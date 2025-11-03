package ua.ohonovskiy.onlineshop.product.service.productImage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.ohonovskiy.onlineshop.product.model.product.Product;
import ua.ohonovskiy.onlineshop.product.model.productImage.ProductImage;
import ua.ohonovskiy.onlineshop.product.repository.product.ProductRepository;
import ua.ohonovskiy.onlineshop.product.repository.productImage.ProductImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public ProductImage addImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String folder = "uploads/products/" + productId;
        try {
            Files.createDirectories(Paths.get(folder));
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(folder, filename);
            Files.write(filePath, file.getBytes());

            ProductImage image = ProductImage.builder()
                    .imageUrl("/" + filePath.toString().replace("\\", "/"))
                    .product(product)
                    .build();

            return productImageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
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
}

