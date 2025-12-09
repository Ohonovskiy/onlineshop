package ua.ohonovskiy.onlineshop.shop.model;

import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.synchronize.model.ImportSubmit;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"products"})
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
