package ua.ohonovskiy.onlineshop.user.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    ROLE_USER,
    ROLE_SHOP_OWNER,
    ROLE_ADMIN,
    ROLE_SYSTEM;

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }
}
