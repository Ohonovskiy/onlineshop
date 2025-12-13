package ua.ohonovskiy.onlineshop.product.product.model;

import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.category.model.Category;
import ua.ohonovskiy.onlineshop.product.productImage.model.ProductImage;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModelNoId;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"category"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class Product extends SimpleEntityModelNoId {

    @Id
    private String id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    private double price;
    private Double oldPrice;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne
    private Category category;

    public void setPrice(double price) {
        if (oldPrice != null && price > oldPrice) {
            oldPrice = null;
            this.price = price;
            return;
        }

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
