package ua.ohonovskiy.onlineshop.homepage.service;

import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.homepage.DTO.HomePageProductsResponse;
import ua.ohonovskiy.onlineshop.product.service.product.ProductService;

@Service
public class RecommendationService {
    private final ProductService productService;
    private final Integer PRODUCT_COUNT = 10;

    public RecommendationService(ProductService productService) {
        this.productService = productService;
    }

    public HomePageProductsResponse getRecommendationProducts() {
        return HomePageProductsResponse.builder()
                .newProducts(productService.getLatest(PRODUCT_COUNT))
                .popularProducts(productService.getPopular(PRODUCT_COUNT))
                .discountedProducts(productService.getDiscounted(PRODUCT_COUNT))
                .build();
    }
}
