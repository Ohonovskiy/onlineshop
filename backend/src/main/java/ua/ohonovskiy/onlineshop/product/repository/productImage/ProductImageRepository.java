package ua.ohonovskiy.onlineshop.product.repository.productImage;

import ua.ohonovskiy.onlineshop.product.model.productImage.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProduct_Id(Long productId);
}
