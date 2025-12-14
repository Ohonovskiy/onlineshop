package ua.ohonovskiy.onlineshop.product.productImage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.product.repository.ProductRepository;
import ua.ohonovskiy.onlineshop.product.productImage.model.ProductImage;
import ua.ohonovskiy.onlineshop.product.productImage.repository.ProductImageRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Value("${app.upload-url:/uploads}")
    private String uploadUrl;

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    // ===================== ТВОЇ МЕТОДИ (як є) =====================

    public ProductImage addImage(String productId, MultipartFile file) {
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

            String webPath = buildWebPath(productId, filename);

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

    public List<ProductImage> addImage(String productId, List<MultipartFile> files) {
        List<ProductImage> result = new ArrayList<>();
        for (MultipartFile f : files) {
            result.add(addImage(productId, f));
        }
        return result;
    }

    public List<ProductImage> getImagesByProduct(String productId) {
        return productImageRepository.findAllByProduct_Id(productId);
    }

    public void deleteImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        try {
            Path filePath = resolveFileSystemPath(image.getImageUrl());
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {}

        productImageRepository.deleteById(imageId);
    }

    public void deleteAllByProduct(String productId) {
        List<ProductImage> images = productImageRepository.findAllByProduct_Id(productId);
        for (ProductImage img : images) {
            try {
                Path filePath = resolveFileSystemPath(img.getImageUrl());
                Files.deleteIfExists(filePath);
            } catch (IOException ignored) {}
        }
        productImageRepository.deleteAll(images);
    }

    public void importImages(Product product, List<String> imageUrls) {
        String productId = product.getId();
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

    // ===================== НОВЕ ДЛЯ BATCH (додано) =====================

    /**
     * Batch save для імпорту (менше insert'ів)
     */
    public void saveAllBatch(List<ProductImage> images) {
        if (images == null || images.isEmpty()) return;
        productImageRepository.saveAll(images);
        productImageRepository.flush();
    }

    /**
     * Скачує картинку, зберігає на диск і повертає ProductImage (але НЕ зберігає в БД),
     * щоб ти міг накопичити список і зробити saveAllBatch().
     */
    public ProductImage downloadAndBuild(Product product, String imageUrl) throws IOException {
        String productId = product.getId();
        Path folder = resolveProductFolder(productId);
        Files.createDirectories(folder);

        String extension = getExtension(imageUrl);
        String filename = UUID.randomUUID() + extension;

        Path filePath = folder.resolve(filename);
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        String webPath = buildWebPath(productId, filename);

        return ProductImage.builder()
                .imageUrl(webPath)
                .product(product)
                .build();
    }

    // ===================== ХЕЛПЕРИ (твої) =====================

    private Path resolveProductFolder(String productId) {
        return Paths.get(uploadDir, "products", String.valueOf(productId));
    }

    private String buildWebPath(String productId, String filename) {
        String base = uploadUrl.startsWith("/") ? uploadUrl : "/" + uploadUrl;
        return base + "/products/" + productId + "/" + filename;
    }

    private Path resolveFileSystemPath(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL is empty");
        }

        String normalizedImageUrl = imageUrl.startsWith("/") ? imageUrl : "/" + imageUrl;
        String normalizedUploadUrl = uploadUrl.startsWith("/") ? uploadUrl : "/" + uploadUrl;

        if (!normalizedImageUrl.startsWith(normalizedUploadUrl)) {
            throw new IllegalStateException("Image URL " + imageUrl +
                    " does not start with uploadUrl " + uploadUrl);
        }

        String relativePart = normalizedImageUrl.substring(normalizedUploadUrl.length());
        return Paths.get(uploadDir, relativePart);
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
}
