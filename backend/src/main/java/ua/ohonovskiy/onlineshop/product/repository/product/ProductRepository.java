package ua.ohonovskiy.onlineshop.product.repository.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.product.model.product.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByCreatedOnDesc(Pageable pageable);

    @Query("""
    SELECT p FROM Product p
    WHERE p.oldPrice IS NOT NULL AND p.oldPrice > p.price
    ORDER BY (p.oldPrice - p.price) / p.oldPrice DESC
    """)
    List<Product> findTopDiscounted(Pageable pageable);

}
