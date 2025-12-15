package ua.ohonovskiy.onlineshop.user.model;

import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.cart.cart.model.Cart;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.user.enums.Role;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"shop"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends SimpleEntityModel {

    private String name;

    private String surname;

    private String password;

    private String email;

    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Shop shop;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.ROLE_USER;
}
