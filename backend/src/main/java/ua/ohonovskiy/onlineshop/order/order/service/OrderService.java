package ua.ohonovskiy.onlineshop.order.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.ohonovskiy.onlineshop.order.order.DTO.PlaceOrderRequest;
import ua.ohonovskiy.onlineshop.order.order.enums.OrderStatus;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.order.order.repository.OrderRepository;
import ua.ohonovskiy.onlineshop.order.orderItem.model.OrderItem;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.product.service.ProductService;
import ua.ohonovskiy.onlineshop.shared.exception.OrderNotExistsException;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.user.service.UserService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final UserService userService;
    private final ProductService productService;

    public List<Order> getAllForCurrentUser() {
        return userService.getCurrentUser().getOrders();
    }

    public Order getForCurrentUserById(Long id) {
        return userService.getCurrentUser().getOrders()
                .stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public Order placeOrderForCurrentUser(PlaceOrderRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Order items are empty");
        }

        List<String> ids = request.items().stream()
                .map(PlaceOrderRequest.Item::productId)
                .distinct()
                .toList();

        List<Product> products = productService.getAllById(ids);

        if (products.size() != ids.size()) {
            throw new IllegalArgumentException("Some products not found");
        }

        Long shopId = products.get(0).getShop().getId();
        boolean sameShop = products.stream().allMatch(p -> shopId.equals(p.getShop().getId()));
        if (!sameShop) {
            throw new IllegalArgumentException("Products are not from the same shop");
        }

        Map<String, Product> byId = products.stream()
                .collect(java.util.stream.Collectors.toMap(Product::getId, p -> p));

        // 4) Створюємо order + items на бекенді
        User user = userService.getCurrentUser();

        Order order = Order.builder()
                .user(user)
                .shop(products.get(0).getShop())
                .comment(request.comment())
                .customerName(request.customerName())
                .customerPhone(request.customerPhone())
                .deliveryAddress(request.deliveryAddress())
                .status(OrderStatus.NEW)
                .build();

        double total = 0;

        for (PlaceOrderRequest.Item reqItem : request.items()) {
            if (reqItem.quantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be > 0");
            }

            Product p = byId.get(reqItem.productId());

            OrderItem item = OrderItem.builder()
                    .product(p)
                    .productName(p.getName())          // snapshot
                    .priceAtPurchase(p.getPrice())     // snapshot
                    .quantity(reqItem.quantity())
                    .build();

            order.addItem(item);

            total += p.getPrice() * reqItem.quantity();
        }

        order.setTotalPrice(total);

        return repository.save(order);
    }


    @Transactional
    public void cancelOrder(Long orderId) {
        User user = userService.getCurrentUser();

        Order order = repository.findByIdAndUser(orderId, user)
                .orElseThrow(() ->
                        new OrderNotExistsException("Order with id: " + orderId + " was not found")
                );

        if (!order.canBeCanceled()) {
            throw new IllegalStateException(
                    "You cannot cancel order with " + order.getStatus() + " status."
            );
        }

        order.setStatus(OrderStatus.CANCELED);
    }

}
