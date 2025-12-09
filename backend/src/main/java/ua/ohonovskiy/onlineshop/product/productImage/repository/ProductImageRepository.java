package ua.ohonovskiy.onlineshop.product.productImage.repository;

import ua.ohonovskiy.onlineshop.product.productImage.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProduct_Id(Long productId);
}
