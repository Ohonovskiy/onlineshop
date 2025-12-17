package ua.ohonovskiy.onlineshop.shared.DTO;

import ua.ohonovskiy.onlineshop.product.product.DTO.ProductDTO;

public record ProductQtyDTO(ProductDTO productDTO, int quantity) {
}
