package ua.ohonovskiy.onlineshop.cart.cart.DTO;

import ua.ohonovskiy.onlineshop.shared.DTO.ProductQtyDTO;

import java.util.List;

public record CartDTO(
        List<ProductQtyDTO> items
) {
}
