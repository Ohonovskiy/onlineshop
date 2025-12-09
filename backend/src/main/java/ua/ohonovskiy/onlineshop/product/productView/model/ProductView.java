package ua.ohonovskiy.onlineshop.product.productView.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductView extends SimpleEntityModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private String sessionId;

    private LocalDateTime viewedAt;
}
