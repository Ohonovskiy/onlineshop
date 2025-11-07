package ua.ohonovskiy.onlineshop.product.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.product.model.product.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByShop_Id(Long shopId);
    boolean existsByIdAndShopId(Long productId, Long shopId);
}
