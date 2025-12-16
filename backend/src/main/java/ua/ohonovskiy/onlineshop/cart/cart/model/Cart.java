package ua.ohonovskiy.onlineshop.cart.cart.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.cart.cartItem.model.CartItem;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;
import ua.ohonovskiy.onlineshop.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(exclude = {"user"})
@JsonIgnoreProperties({"user"})
public class Cart extends SimpleEntityModel {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();
}
