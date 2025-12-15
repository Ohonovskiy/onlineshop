package ua.ohonovskiy.onlineshop.product.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.category.model.Category;
import ua.ohonovskiy.onlineshop.product.productImage.model.ProductImage;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModelNoId;
import ua.ohonovskiy.onlineshop.shop.model.Shop;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"category"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Shop shop;

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
