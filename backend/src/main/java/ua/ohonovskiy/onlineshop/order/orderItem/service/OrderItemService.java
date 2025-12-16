package ua.ohonovskiy.onlineshop.order.orderItem.service;

import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.order.orderItem.DTO.ItemDTO;
import ua.ohonovskiy.onlineshop.order.orderItem.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    public ItemDTO toItemDTO(OrderItem item) {
        return new ItemDTO(
                item.getProduct() != null
                        ? item.getProduct().getId()
                        : null,
                item.getQuantity()
        );
    }

    public List<ItemDTO> toItemDTO(List<OrderItem> items) {
        return items.stream().map(this::toItemDTO).collect(Collectors.toList());
    }
}
