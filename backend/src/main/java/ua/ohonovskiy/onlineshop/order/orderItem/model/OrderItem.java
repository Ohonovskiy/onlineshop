package ua.ohonovskiy.onlineshop.order.orderItem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(exclude = {"order", "product"})
@JsonIgnoreProperties({"order", "product"})
public class OrderItem extends SimpleEntityModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private BigDecimal priceAtPurchase;

    @Column(nullable = false)
    private int quantity;
}

