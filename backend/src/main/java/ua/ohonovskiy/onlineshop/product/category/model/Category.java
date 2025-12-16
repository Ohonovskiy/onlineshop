package ua.ohonovskiy.onlineshop.product.category.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Category extends SimpleEntityModel {
    private String name;

    @ManyToOne
    private Category parent;
}
