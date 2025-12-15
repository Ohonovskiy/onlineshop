package ua.ohonovskiy.onlineshop.order.order.DTO;

import java.util.List;

public record PlaceOrderRequest(
        List<Item> items,
        String comment,
        String customerName,
        String customerPhone,
        String deliveryAddress
) {
    public record Item(String productId, int quantity) {}
}
