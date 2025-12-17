package ua.ohonovskiy.onlineshop.order.orderItem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.product.product.service.ProductService;
import ua.ohonovskiy.onlineshop.shared.DTO.ProductQtyDTO;
import ua.ohonovskiy.onlineshop.order.orderItem.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final ProductService productService;

    public ProductQtyDTO toItemDTO(OrderItem item) {
        return new ProductQtyDTO(
                item.getProduct() != null
                        ? productService.toDTO(item.getProduct())
                        : null,
                item.getQuantity()
        );
    }

    public List<ProductQtyDTO> toItemDTO(List<OrderItem> items) {
        return items.stream().map(this::toItemDTO).collect(Collectors.toList());
    }
}
