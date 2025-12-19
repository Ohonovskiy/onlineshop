package ua.ohonovskiy.onlineshop.product.productView.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.product.repository.ProductRepository;
import ua.ohonovskiy.onlineshop.product.productView.model.ProductView;
import ua.ohonovskiy.onlineshop.product.productView.repository.ProductViewRepository;
import ua.ohonovskiy.onlineshop.user.model.CustomUserDetails;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductViewService {
    private static final int VIEW_INTERVAL_MINUTES = 30;

    private final ProductViewRepository productViewRepository;
    private final ProductRepository productRepository;

    public long getForProductFrom(Product product, LocalDateTime from) {
        return getForProductFrom(product.getId(), from);
    }

    public long getForProductFrom(String productId, LocalDateTime from) {
        return productViewRepository.countViewsForProductFrom(productId, from);
    }

    public void registerView(String productId, CustomUserDetails userDetails) {

        if (userDetails == null || userDetails.getUsername() == null) {
            return;
        }

        String username = userDetails.getUsername();

        if (isRegisteredAlreadyFrom(
                productId,
                username,
                LocalDateTime.now().minusMinutes(VIEW_INTERVAL_MINUTES))) {
            return;
        }
        
        Product product = productRepository.getReferenceById(productId);

        productViewRepository.save(
                ProductView.builder()
                        .product(product)
                        .username(username)
                        .viewedAt(LocalDateTime.now())
                        .build()
        );
    }


    private boolean isRegisteredAlreadyFrom(String productId,
                                            String username,
                                            LocalDateTime from) {
        return productViewRepository.countViews(productId, username, from) > 0;
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
