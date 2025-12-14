package ua.ohonovskiy.onlineshop.cart.DTO;

public record ProductQuantityRequest(
        String productId,
        int qty
)
{}
