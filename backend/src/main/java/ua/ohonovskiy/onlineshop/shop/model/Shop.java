package ua.ohonovskiy.onlineshop.shop.model;

import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.synchronize.model.ImportSubmit;
import ua.ohonovskiy.onlineshop.util.exception.ImportNotExistsException;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

import java.util.ArrayList;
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

    @OneToMany
    @JoinColumn(name = "import_id")
    private List<ImportSubmit> importSubmits;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

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
