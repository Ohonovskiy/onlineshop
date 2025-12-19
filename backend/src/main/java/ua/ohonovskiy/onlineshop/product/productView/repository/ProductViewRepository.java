package ua.ohonovskiy.onlineshop.product.productView.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.ohonovskiy.onlineshop.product.productView.model.ProductView;

import java.time.LocalDateTime;

@Repository
public interface ProductViewRepository extends JpaRepository<ProductView, Long> {
    @Query("""
        SELECT COUNT(v)
        FROM ProductView v
        WHERE v.product.id = :productId
          AND v.viewedAt >= :from
        """)
    long countViewsForProductFrom(
            @Param("productId") String productId,
            @Param("from") LocalDateTime from
    );

    @Query("""
    SELECT COUNT(v)
    FROM ProductView v
    WHERE v.product.id = :productId
      AND v.username = :username
      AND v.viewedAt >= :from
    """)
    long countViews(
            @Param("productId") String productId,
            @Param("username") String username,
            @Param("from") LocalDateTime from
    );

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM ProductView v
    WHERE v.viewedAt < :before
    """)
    void deleteAllBefore(@Param("before") LocalDateTime before);
}
