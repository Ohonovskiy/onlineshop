package ua.ohonovskiy.onlineshop.cart.repository.cartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ohonovskiy.onlineshop.cart.model.cartItem.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
