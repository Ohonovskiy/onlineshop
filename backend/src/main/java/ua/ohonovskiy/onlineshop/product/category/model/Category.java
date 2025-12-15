package ua.ohonovskiy.onlineshop.product.category.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;

@EqualsAndHashCode(callSuper = true, exclude = {"parent"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category extends SimpleEntityModel {
    private String name;

    @ManyToOne
    private Category parent;
}
