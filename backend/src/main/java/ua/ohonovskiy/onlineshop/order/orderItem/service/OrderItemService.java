package ua.ohonovskiy.onlineshop.order.orderItem.service;

import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.shared.DTO.ProductIdQtyDTO;
import ua.ohonovskiy.onlineshop.order.orderItem.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    public ProductIdQtyDTO toItemDTO(OrderItem item) {
        return new ProductIdQtyDTO(
                item.getProduct() != null
                        ? item.getProduct().getId()
                        : null,
                item.getQuantity()
        );
    }

    public List<ProductIdQtyDTO> toItemDTO(List<OrderItem> items) {
        return items.stream().map(this::toItemDTO).collect(Collectors.toList());
    }
}
