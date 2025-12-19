package ua.ohonovskiy.onlineshop.order.order.DTO;

import ua.ohonovskiy.onlineshop.order.order.model.UserOrderInfo;
import ua.ohonovskiy.onlineshop.shared.DTO.ItemQtyDTO;

import java.util.List;

public record PlaceOrderRequest (
        List<ItemQtyDTO> items,
        UserOrderInfo userOrderInfo
) {
}
