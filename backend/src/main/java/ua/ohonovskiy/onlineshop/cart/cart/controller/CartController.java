package ua.ohonovskiy.onlineshop.cart.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ohonovskiy.onlineshop.cart.DTO.ProductQuantityRequest;
import ua.ohonovskiy.onlineshop.cart.cart.model.Cart;
import ua.ohonovskiy.onlineshop.cart.cart.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getAll() {
        return ResponseEntity.ok(cartService.getForCurrentUser());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCart() {
        cartService.deleteForCurrentUser();

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addToCart(@RequestBody ProductQuantityRequest productQuantityRequest) {
        cartService.addForCurrentUser(productQuantityRequest);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteFromCart(@PathVariable String id) {
        cartService.removeProduct(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PatchMapping("/items")
    public ResponseEntity<Void> change(@RequestBody ProductQuantityRequest request) {
        cartService.changeQuantity(request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
