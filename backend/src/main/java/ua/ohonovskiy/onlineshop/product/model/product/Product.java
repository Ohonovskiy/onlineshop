package ua.ohonovskiy.onlineshop.product.model.product;

import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.model.category.Category;
import ua.ohonovskiy.onlineshop.product.model.productImage.ProductImage;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"category"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class Product extends SimpleEntityModel {
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    private double price;
    private double oldPrice;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne
    private Category category;

    public void setPrice(double price) {
        if(price > oldPrice) return;

        this.oldPrice = this.price;
        this.price = price;
    }

    public void addProductImage(ProductImage image) {
        image.setProduct(this);
        productImages.add(image);
    }

    public void removeProductImage(ProductImage image) {
        productImages.removeIf(i -> i.getId().equals(image.getId()));
    }
}
