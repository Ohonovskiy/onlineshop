package ua.ohonovskiy.onlineshop.order.order.DTO;

import lombok.Builder;
import ua.ohonovskiy.onlineshop.order.order.model.UserOrderInfo;
import ua.ohonovskiy.onlineshop.order.orderItem.DTO.ItemDTO;
import ua.ohonovskiy.onlineshop.order.orderItem.model.OrderItem;

import java.util.List;

@Builder
public record OrderDTO(
        List<ItemDTO> itemsDTO,
        UserOrderInfo userInfo
) {

    private ItemDTO toItemDTO(OrderItem item) {
        return new ItemDTO(
                item.getProduct() != null
                        ? item.getProduct().getId()
                        : null,
                item.getQuantity()
        );
    }

}
