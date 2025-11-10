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
    private String uploadDir;

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public ProductImage addImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String folder = uploadDir+"/products/" + productId;
        try {
            Files.createDirectories(Paths.get(folder));
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(folder, filename);
            Files.write(filePath, file.getBytes());

            ProductImage image = ProductImage.builder()
                    .imageUrl("/" + filePath.toString().replace("\\", "/"))
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
            Files.deleteIfExists(Paths.get(image.getImageUrl().replaceFirst("/", "")));
        } catch (IOException ignored) {}
        productImageRepository.deleteById(imageId);
    }

    public void deleteAllByProduct(Long productId) {
        List<ProductImage> images = productImageRepository.findAllByProduct_Id(productId);
        for (ProductImage img : images) {
            try {
                Files.deleteIfExists(Paths.get(img.getImageUrl().replaceFirst("/", "")));
            } catch (IOException ignored) {}
        }
        productImageRepository.deleteAll(images);
    }

    public void importImages(Product product, List<String> imageUrls) {
        String folder = uploadDir + "/products/" + product.getId();

        for (String imageUrl : imageUrls) {
            try (InputStream in = new URL(imageUrl).openStream()) {
                Files.createDirectories(Paths.get(folder));

                String extension = imageUrl.contains(".") ?
                        imageUrl.substring(imageUrl.lastIndexOf('.')) : ".jpg";

                String filename = UUID.randomUUID() + extension;
                Path filePath = Paths.get(folder, filename);

                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);

                ProductImage image = ProductImage.builder()
                        .imageUrl("/" + filePath.toString().replace("\\", "/"))
                        .product(product)
                        .build();

                product.addProductImage(image);
                productImageRepository.save(image);

            } catch (IOException e) {
                System.err.println("❌ Failed to download image: " + imageUrl + " → " + e.getMessage());
            }
        }
    }

}

