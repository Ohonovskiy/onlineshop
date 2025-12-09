package ua.ohonovskiy.onlineshop.product.productImage.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

@EqualsAndHashCode(callSuper = true, exclude = {"product"})
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductImage extends SimpleEntityModel {

    private String imageUrl;

    private int orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
}
