package ua.ohonovskiy.onlineshop.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.shared.exception.ImportNotExistsException;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;
import ua.ohonovskiy.onlineshop.synchronize.model.ImportSubmit;
import ua.ohonovskiy.onlineshop.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(exclude = {"owner"})
@JsonIgnoreProperties({"owner"})
public class Shop extends SimpleEntityModel {
    private String name;

    private String description;

    private String importFileURL;

    @OneToMany
    private List<ImportSubmit> importSubmits;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private User owner;

    public void addProduct(Product product) {
        products.add(product);
        product.setShop(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.setShop(null);
    }

    public void addImportSubmit(ImportSubmit importSubmit) {
        if(this.importSubmits == null) this.importSubmits = new ArrayList<>();

        this.importSubmits.add(importSubmit);
    }

    public void removeImportSubmit(ImportSubmit importSubmit) {
        if(this.importSubmits == null) return;

        this.importSubmits.removeIf(i -> i.equals(importSubmit));
        importSubmit.setShop(null);
    }

    public void removeImportSubmit(Long id) {
        if (this.importSubmits == null) return;

        ImportSubmit submit =
                this.importSubmits
                        .stream()
                        .filter(i -> i.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new ImportNotExistsException("ImportSubmit was not found."));

        this.importSubmits.remove(submit);
        submit.setShop(null);
    }
}
