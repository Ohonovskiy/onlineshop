package ua.ohonovskiy.onlineshop.user.model;

import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.user.enums.Role;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends SimpleEntityModel {

    private String name;

    private String surname;

    private String password;

    private String email;

    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Shop shop;

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.ROLE_USER;
}
