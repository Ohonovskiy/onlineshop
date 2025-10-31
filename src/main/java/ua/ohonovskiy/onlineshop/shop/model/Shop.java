package ua.ohonovskiy.onlineshop.shop.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import ua.ohonovskiy.onlineshop.product.model.product.Product;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

import java.util.List;

@Entity
public class Shop extends SimpleEntityModel {
    private String name;

    private String description;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
