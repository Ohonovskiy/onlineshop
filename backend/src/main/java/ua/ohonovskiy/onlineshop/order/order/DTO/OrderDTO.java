package ua.ohonovskiy.onlineshop.order.order.DTO;

import lombok.Builder;
import ua.ohonovskiy.onlineshop.order.order.model.UserOrderInfo;
import ua.ohonovskiy.onlineshop.shared.DTO.ProductIdQtyDTO;
import ua.ohonovskiy.onlineshop.order.orderItem.model.OrderItem;

import java.util.List;

@Builder
public record OrderDTO(
        List<ProductIdQtyDTO> itemsDTO,
        UserOrderInfo userInfo
) {

    private ProductIdQtyDTO toItemDTO(OrderItem item) {
        return new ProductIdQtyDTO(
                item.getProduct() != null
                        ? item.getProduct().getId()
                        : null,
                item.getQuantity()
        );
    }

}
