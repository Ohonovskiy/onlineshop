package ua.ohonovskiy.onlineshop.order.order.DTO;

import ua.ohonovskiy.onlineshop.order.order.model.UserOrderInfo;
import ua.ohonovskiy.onlineshop.order.orderItem.DTO.ItemQty;

import java.util.List;

public record PlaceOrderRequest (
        List<ItemQty> items,
        UserOrderInfo userOrderInfo
) {
}
