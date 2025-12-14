package ua.ohonovskiy.onlineshop.cart.service.cartItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.cart.model.cartItem.CartItem;
import ua.ohonovskiy.onlineshop.cart.repository.cartItem.CartItemRepository;
import ua.ohonovskiy.onlineshop.product.product.service.ProductService;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }
}
