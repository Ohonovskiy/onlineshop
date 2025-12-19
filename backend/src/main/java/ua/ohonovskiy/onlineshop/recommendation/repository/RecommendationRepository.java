package ua.ohonovskiy.onlineshop.recommendation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.order.order.enums.OrderStatus;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.shared.DTO.ItemOrdersDTO;
import ua.ohonovskiy.onlineshop.shared.DTO.ItemViewsDTO;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Product, Long> {
    // Get most viewed product ids from
    @Query("""
    SELECT new ua.ohonovskiy.onlineshop.shared.DTO.ItemViewsDTO(
        v.product.id,
        CAST(COUNT(v) AS int)
    )
    FROM ProductView v
    WHERE v.viewedAt >= :from
    GROUP BY v.product.id
    ORDER BY COUNT(v) DESC
""")
    List<ItemViewsDTO> findPopularByViewsFrom(
            Pageable pageable,
            @Param("from") LocalDateTime from
    );

    @Query("""
    SELECT new ua.ohonovskiy.onlineshop.shared.DTO.ItemOrdersDTO(
        oi.product.id,
        CAST(COALESCE(SUM(oi.quantity), 0) AS int)
    )
    FROM OrderItem oi
    WHERE oi.order.createdAt >= :from
      AND oi.order.status IN :paidStatuses
    GROUP BY oi.product.id
    ORDER BY COALESCE(SUM(oi.quantity), 0) DESC
""")
    List<ItemOrdersDTO> findPopularByOrdersFrom(
            Pageable pageable,
            @Param("from") LocalDateTime from,
            @Param("paidStatuses") Collection<OrderStatus> paidStatuses
    );
}
