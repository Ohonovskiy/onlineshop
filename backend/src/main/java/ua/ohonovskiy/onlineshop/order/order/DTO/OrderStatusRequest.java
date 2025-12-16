package ua.ohonovskiy.onlineshop.order.order.DTO;

import ua.ohonovskiy.onlineshop.order.order.enums.OrderStatus;

public record OrderStatusRequest(
        OrderStatus orderStatus
) {
}
