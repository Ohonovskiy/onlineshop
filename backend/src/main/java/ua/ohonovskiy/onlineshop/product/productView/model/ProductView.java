package ua.ohonovskiy.onlineshop.product.productView.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(exclude = {"product"})
public class ProductView extends SimpleEntityModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private String username;

    private LocalDateTime viewedAt;
}
