package ua.ohonovskiy.onlineshop.product.productView.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.product.repository.ProductRepository;
import ua.ohonovskiy.onlineshop.product.product.service.ProductService;
import ua.ohonovskiy.onlineshop.product.productView.model.ProductView;
import ua.ohonovskiy.onlineshop.product.productView.repository.ProductViewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductViewService {
    private static final int VIEW_INTERVAL_MINUTES = 30;

    private final ProductViewRepository productViewRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    public long getForProductFrom(Product product, LocalDateTime from) {
        return getForProductFrom(product.getId(), from);
    }

    public long getForProductFrom(Long productId, LocalDateTime from) {
        return productViewRepository.countViewsForProductFrom(productId, from);
    }

    public List<Long> getTopMostViewedFrom(int amount, LocalDateTime from) {
        return productViewRepository.findPopularProductIdsFrom(
                PageRequest.of(0, amount),
                from
        );
    }

    public void registerView(Long productId, String sessionId) {

        if (sessionId == null || sessionId.isBlank()) {
            return;
        }

        // Dont register frequent requests
        if (isRegisteredAlreadyFrom(productId, sessionId, LocalDateTime.now().minusMinutes(VIEW_INTERVAL_MINUTES))) {
            return;
        }

        // беремо легкий proxy, не вантажимо продукт повністю
        Product product = productRepository.getReferenceById(productId);

        productViewRepository.save(
                ProductView.builder()
                        .product(product)
                        .sessionId(sessionId)
                        .viewedAt(LocalDateTime.now())
                        .build()
        );
    }


    private boolean isRegisteredAlreadyFrom(Long productId, String sessionId, LocalDateTime from) {
        return productViewRepository.countViews(productId, sessionId, from) > 0;
    }

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void scheduler() {
        deleteAllBefore(LocalDateTime.now().minusDays(7));
    }

    protected void deleteAllBefore(LocalDateTime before) {
        productViewRepository.deleteAllBefore(before);
    }
}
