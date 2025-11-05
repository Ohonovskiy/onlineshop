package ua.ohonovskiy.onlineshop.product.model.product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.model.category.Category;
import ua.ohonovskiy.onlineshop.product.model.productImage.ProductImage;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Product extends SimpleEntityModel {
    private String name;
    private String description;
    private double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne
    private Shop shop;

    @ManyToOne
    private Category category;

    public void addProductImage(ProductImage image) {
        image.setProduct(this);
        productImages.add(image);
    }

    public void removeProductImage(ProductImage image) {
        productImages.removeIf(i -> i.getId().equals(image.getId()));
    }
}
