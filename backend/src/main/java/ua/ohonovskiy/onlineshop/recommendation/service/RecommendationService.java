package ua.ohonovskiy.onlineshop.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.homepage.DTO.HomePageProductsResponse;
import ua.ohonovskiy.onlineshop.order.order.enums.OrderStatus;
import ua.ohonovskiy.onlineshop.product.product.DTO.ProductDTO;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.product.service.ProductService;
import ua.ohonovskiy.onlineshop.recommendation.repository.RecommendationRepository;
import ua.ohonovskiy.onlineshop.shared.DTO.ItemOrdersDTO;
import ua.ohonovskiy.onlineshop.shared.DTO.ItemViewsDTO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final Integer PRODUCT_COUNT = 10;

    private final Integer VIEW_WEIGHT = 1;
    private final Integer ORDER_WEIGHT = 15;

    private final ProductService productService;
    private final RecommendationRepository recommendationRepository;

    public HomePageProductsResponse getRecommendationProducts() {
        return HomePageProductsResponse.builder()
                .newProducts(productService.getLatest(PRODUCT_COUNT))
                .popularProducts(getPopular(PRODUCT_COUNT))
                .discountedProducts(productService.getTopDiscounted(PRODUCT_COUNT))
                .build();
    }

    public List<ProductDTO> getPopular(Integer productsCount) {
        int limit = (productsCount == null || productsCount <= 0) ? 10 : productsCount;

        LocalDateTime from = LocalDateTime.now().minusDays(7);

        List<ItemViewsDTO> mostViewed = getTopMostViewedFrom(limit * 2, from);
        List<ItemOrdersDTO> mostOrdered = getTopMostOrderedFrom(limit * 2, from, getPopularMeetingStatuses());

        Map<String, Integer> viewsMap = mostViewed.stream()
                .collect(Collectors.toMap(ItemViewsDTO::productId, ItemViewsDTO::viewsAmount, Integer::sum));

        Map<String, Integer> ordersMap = mostOrdered.stream()
                .collect(Collectors.toMap(ItemOrdersDTO::productId, ItemOrdersDTO::ordersAmount, Integer::sum));

        // всі кандидати = union двох топів
        Set<String> candidates = new HashSet<>();
        candidates.addAll(viewsMap.keySet());
        candidates.addAll(ordersMap.keySet());

        // порахувати score і взяти top limit
        List<String> topIds = candidates.stream()
                .sorted(Comparator.<String>comparingLong(id ->
                        (long) viewsMap.getOrDefault(id, 0) * VIEW_WEIGHT +
                                (long) ordersMap.getOrDefault(id, 0) * ORDER_WEIGHT
                ).reversed())
                .limit(limit)
                .toList();

        // дістати продукти (findAllById порядок не гарантує)
        List<Product> products = productService.getAllById(topIds);
        Map<String, Product> byId = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        return topIds.stream()
                .map(byId::get)
                .filter(Objects::nonNull)
                .map(productService::toDTO)   // або твій toDTO
                .toList();
    }

    private List<OrderStatus> getPopularMeetingStatuses() {
        return List.of(
                OrderStatus.PAID,
                OrderStatus.SHIPPED,
                OrderStatus.DELIVERED
        );
    }

    public List<ItemViewsDTO> getTopMostViewedFrom(int amount, LocalDateTime from) {
        return recommendationRepository.findPopularByViewsFrom(
                PageRequest.of(0, amount),
                from);
    }

    public List<ItemOrdersDTO> getTopMostOrderedFrom(int amount, LocalDateTime from, List<OrderStatus> statusList) {
        return recommendationRepository.findPopularByOrdersFrom(
                PageRequest.of(0, amount),
                from,
                statusList
        );
    }
}
