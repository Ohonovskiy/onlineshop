package ua.ohonovskiy.onlineshop.user.model;

import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.cart.cart.model.Cart;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.model.RecipientInfo;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.model.SenderInfo;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.user.enums.Role;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class User extends SimpleEntityModel {

    private String name;

    private String surname;

    private String password;

    private String email;

    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    private SenderInfo senderInfo;

    private RecipientInfo recipientInfo;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_USER;
}
