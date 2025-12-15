package ua.ohonovskiy.onlineshop.cart.cart.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.cart.DTO.ProductQuantityRequest;
import ua.ohonovskiy.onlineshop.cart.cart.model.Cart;
import ua.ohonovskiy.onlineshop.cart.cartItem.model.CartItem;
import ua.ohonovskiy.onlineshop.cart.cart.repository.CartRepository;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.product.service.ProductService;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.user.service.UserService;
import ua.ohonovskiy.onlineshop.shared.exception.ProductNotExistsException;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }


    public void deleteForCurrentUser() {
        User user = userService.getCurrentUser();
        user.setCart(new Cart(user, new ArrayList<>()));

        userService.save(user);
    }

    public Cart getForCurrentUser() {
        User user = userService.getCurrentUser();
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user, new ArrayList<>())));
    }

    @Transactional
    public void addForCurrentUser(ProductQuantityRequest productQuantityRequest) {
        String productId = productQuantityRequest.productId();
        int qty = productQuantityRequest.qty();

        User user = userService.getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user, new ArrayList<>())));

        CartItem item = cart.getItems()
                .stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            Product product = productService.getById(productId)
                    .orElseThrow(() ->
                            new ProductNotExistsException("Can't find product with id: " + productId)
                    );
            item = new CartItem(cart, product, qty);
            cart.getItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + qty);
        }

        save(cart);
        userService.save(user);
    }

    @Transactional
    public void removeProduct(String productId) {
        Cart cart = cartRepository.findByUser(userService.getCurrentUser())
                .orElseThrow();

        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        save(cart);
    }

    @Transactional
    public void changeQuantity(ProductQuantityRequest request) {
        Cart cart = cartRepository.findByUser(userService.getCurrentUser())
                .orElseThrow();

        cart.getItems().forEach(i -> {
            if (i.getProduct().getId().equals(request.productId())) {
                i.setQuantity(request.qty());
            }
        });

        save(cart);
    }
}
