package ua.ohonovskiy.onlineshop.order.order.DTO;

import lombok.Builder;
import ua.ohonovskiy.onlineshop.order.order.model.UserOrderInfo;
import ua.ohonovskiy.onlineshop.shared.DTO.ProductIdQtyDTO;

import java.util.List;

@Builder
public record OrderDTO(
        List<ProductIdQtyDTO> itemsDTO,
        UserOrderInfo userInfo
) {}
