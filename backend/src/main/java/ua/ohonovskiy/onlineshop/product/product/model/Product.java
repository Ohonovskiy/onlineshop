package ua.ohonovskiy.onlineshop.product.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.category.model.Category;
import ua.ohonovskiy.onlineshop.product.productImage.model.ProductImage;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModelNoId;
import ua.ohonovskiy.onlineshop.shop.model.Shop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true, exclude = {"category"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "shop"})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"shop"})
public class Product extends SimpleEntityModelNoId {

    @Id
    private String id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    private BigDecimal price;
    private BigDecimal oldPrice;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    public void setPrice(BigDecimal newPrice) {
        if (newPrice == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        if (this.price == null) {
            this.price = newPrice;
            this.oldPrice = null;
            return;
        }

        if (newPrice.compareTo(this.price) < 0) {
            this.oldPrice = this.price;
            this.price = newPrice;
            return;
        }

        this.price = newPrice;
        this.oldPrice = null;
    }


    public void addProductImage(ProductImage image) {
        image.setProduct(this);
        productImages.add(image);
    }

    public void removeProductImage(ProductImage image) {
        productImages.removeIf(i -> i.getId().equals(image.getId()));
    }
}
