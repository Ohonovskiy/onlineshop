package ua.ohonovskiy.onlineshop.cart.cart.DTO;

import ua.ohonovskiy.onlineshop.shared.DTO.ProductIdQtyDTO;

import java.util.List;

public record CartDTO(
        List<ProductIdQtyDTO> items
) {
}
