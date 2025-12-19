package ua.ohonovskiy.onlineshop.order.order.DTO;

import lombok.Builder;
import lombok.Data;
import ua.ohonovskiy.onlineshop.order.order.enums.OrderStatus;
import ua.ohonovskiy.onlineshop.order.order.model.UserOrderInfo;
import ua.ohonovskiy.onlineshop.shared.DTO.ProductQtyDTO;

import java.util.List;

@Builder
@Data
public class OrderDTO {
    List<ProductQtyDTO> productQtyDTOList;
    UserOrderInfo userInfo;
    OrderStatus orderStatus;
}
