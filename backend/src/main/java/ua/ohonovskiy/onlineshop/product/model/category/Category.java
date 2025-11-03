package ua.ohonovskiy.onlineshop.product.model.category;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category extends SimpleEntityModel {
    private String name;

    @ManyToOne
    private Category parent;
}
