package ua.ohonovskiy.onlineshop.cart.cartItem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.cart.cartItem.model.CartItem;
import ua.ohonovskiy.onlineshop.cart.cartItem.repository.CartItemRepository;
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
