package ua.ohonovskiy.onlineshop.product.productView.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.product.productView.model.ProductView;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductViewRepository extends JpaRepository<ProductView, Long> {
    @Query("""
        SELECT COUNT(v)
        FROM ProductView v
        WHERE v.product.id = :productId
          AND v.viewedAt >= :from
        """)
    long countViewsForProductFrom(
            @Param("productId") Long productId,
            @Param("from") LocalDateTime from
    );

    // Get most viewed product ids from
    @Query("""
        SELECT v.product.id
        FROM ProductView v
        WHERE v.viewedAt >= :from
        GROUP BY v.product.id
        ORDER BY COUNT(v) DESC
    """)
    List<Long> findPopularProductIdsFrom(
            Pageable pageable,
            @Param("from") LocalDateTime from
    );

    @Query("""
    SELECT COUNT(v)
    FROM ProductView v
    WHERE v.product.id = :productId
      AND v.sessionId = :sessionId
      AND v.viewedAt >= :from
    """)
    long countViews(
            @Param("productId") Long productId,
            @Param("sessionId") String sessionId,
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
