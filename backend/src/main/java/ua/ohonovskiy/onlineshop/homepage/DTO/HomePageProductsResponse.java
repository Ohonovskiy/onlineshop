package ua.ohonovskiy.onlineshop.homepage.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ua.ohonovskiy.onlineshop.product.product.DTO.ProductDTO;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomePageProductsResponse {
    private List<ProductDTO> newProducts = new ArrayList<>();
    private List<ProductDTO> popularProducts = new ArrayList<>();
    private List<ProductDTO> discountedProducts = new ArrayList<>();
}
