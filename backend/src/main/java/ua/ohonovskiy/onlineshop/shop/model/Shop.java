package ua.ohonovskiy.onlineshop.shop.model;

import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.model.product.Product;
import ua.ohonovskiy.onlineshop.synchronize.model.ImportSubmit;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"owner", "products"})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Shop extends SimpleEntityModel {
    private String name;

    private String description;

    private String importFileURL;

    @OneToOne
    @JoinColumn(name = "import_id")
    private ImportSubmit ImportSubmit;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
