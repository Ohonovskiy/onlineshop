package ua.ohonovskiy.onlineshop.product.service.productImage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.ohonovskiy.onlineshop.product.model.product.Product;
import ua.ohonovskiy.onlineshop.product.model.productImage.ProductImage;
import ua.ohonovskiy.onlineshop.product.repository.product.ProductRepository;
import ua.ohonovskiy.onlineshop.product.repository.productImage.ProductImageRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    @Value("${app.upload-dir}")
    private String uploadDir;     // Абсолютний шлях на диску, наприклад: C:/.../uploads

    @Value("${app.upload-url:/uploads}")
    private String uploadUrl;     // Web-корінь, наприклад: /uploads

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    // ===================== ПУБЛІЧНІ МЕТОДИ =====================

    public ProductImage addImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        try {
            Path folder = resolveProductFolder(productId);
            Files.createDirectories(folder);

            String originalName = file.getOriginalFilename();
            String extension = getExtension(originalName);
            String filename = UUID.randomUUID() + extension;

            Path filePath = folder.resolve(filename);
            Files.write(filePath, file.getBytes());

            String webPath = buildWebPath(productId, filename); // шлях, який піде в БД

            ProductImage image = ProductImage.builder()
                    .imageUrl(webPath)
                    .product(product)
                    .build();

            product.addProductImage(image);

            return productImageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    public List<ProductImage> addImage(Long productId, List<MultipartFile> files) {
        List<ProductImage> result = new ArrayList<>();
        for (MultipartFile f : files) {
            result.add(addImage(productId, f));
        }
        return result;
    }

    public List<ProductImage> getImagesByProduct(Long productId) {
        return productImageRepository.findAllByProduct_Id(productId);
    }

    public void deleteImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        try {
            Path filePath = resolveFileSystemPath(image.getImageUrl());
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {
        }

        productImageRepository.deleteById(imageId);
    }

    public void deleteAllByProduct(Long productId) {
        List<ProductImage> images = productImageRepository.findAllByProduct_Id(productId);
        for (ProductImage img : images) {
            try {
                Path filePath = resolveFileSystemPath(img.getImageUrl());
                Files.deleteIfExists(filePath);
            } catch (IOException ignored) {
            }
        }
        productImageRepository.deleteAll(images);
    }

    public void importImages(Product product, List<String> imageUrls) {
        Long productId = product.getId();
        Path folder = resolveProductFolder(productId);

        for (String imageUrl : imageUrls) {
            try (InputStream in = new URL(imageUrl).openStream()) {
                Files.createDirectories(folder);

                String extension = getExtension(imageUrl);
                String filename = UUID.randomUUID() + extension;

                Path filePath = folder.resolve(filename);
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);

                String webPath = buildWebPath(productId, filename);

                ProductImage image = ProductImage.builder()
                        .imageUrl(webPath)
                        .product(product)
                        .build();

                product.addProductImage(image);
                productImageRepository.save(image);

            } catch (IOException e) {
                System.err.println("❌ Failed to download image: " + imageUrl + " → " + e.getMessage());
            }
        }
    }

    // ===================== ПРИВАТНІ ХЕЛПЕРИ =====================

    /**
     * Папка для зберігання файлів конкретного продукту на диску.
     * Приклад: C:/.../uploads/products/5
     */
    private Path resolveProductFolder(Long productId) {
        return Paths.get(uploadDir, "products", String.valueOf(productId));
    }

    /**
     * Web-шлях, який ми зберігаємо в БД і віддаємо фронту.
     * Приклад: /uploads/products/5/filename.jpg
     */
    private String buildWebPath(Long productId, String filename) {
        String base = uploadUrl.startsWith("/") ? uploadUrl : "/" + uploadUrl;
        return base + "/products/" + productId + "/" + filename;
    }

    /**
     * Перетворює web-шлях з БД назад у шлях на файловій системі.
     * imageUrl:  /uploads/products/5/filename.jpg
     * uploadUrl: /uploads
     * →         C:/.../uploads/products/5/filename.jpg
     */
    private Path resolveFileSystemPath(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL is empty");
        }

        String normalizedImageUrl = imageUrl.startsWith("/") ? imageUrl : "/" + imageUrl;
        String normalizedUploadUrl = uploadUrl.startsWith("/") ? uploadUrl : "/" + uploadUrl;

        if (!normalizedImageUrl.startsWith(normalizedUploadUrl)) {
            // Якщо раптом у БД щось інше, кидаємо помилку, щоб не видалити не те
            throw new IllegalStateException("Image URL " + imageUrl +
                    " does not start with uploadUrl " + uploadUrl);
        }

        String relativePart = normalizedImageUrl.substring(normalizedUploadUrl.length()); // /products/5/file.jpg
        return Paths.get(uploadDir, relativePart);
    }

    /**
     * Витягує розширення з імені файлу.
     * Якщо немає точки — повертає ".jpg".
     */
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
}
