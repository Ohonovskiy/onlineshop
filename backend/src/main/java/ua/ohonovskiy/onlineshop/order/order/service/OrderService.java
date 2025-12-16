package ua.ohonovskiy.onlineshop.order.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.ohonovskiy.onlineshop.order.order.DTO.OrderDTO;
import ua.ohonovskiy.onlineshop.order.order.enums.OrderStatus;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.order.order.repository.OrderRepository;
import ua.ohonovskiy.onlineshop.shared.DTO.ProductIdQtyDTO;
import ua.ohonovskiy.onlineshop.order.orderItem.model.OrderItem;
import ua.ohonovskiy.onlineshop.order.orderItem.service.OrderItemService;
import ua.ohonovskiy.onlineshop.product.product.model.Product;
import ua.ohonovskiy.onlineshop.product.product.service.ProductService;
import ua.ohonovskiy.onlineshop.shared.exception.OrderNotExistsException;
import ua.ohonovskiy.onlineshop.user.enums.Role;
import ua.ohonovskiy.onlineshop.user.model.User;
import ua.ohonovskiy.onlineshop.user.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final UserService userService;
    private final ProductService productService;
    private final OrderItemService orderItemService;

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
    public Order placeOrderForCurrentUser(OrderDTO request) {
        if (request.itemsDTO() == null || request.itemsDTO().isEmpty()) {
            throw new IllegalArgumentException("Order itemsDTO are empty");
        }

        List<String> ids = request.itemsDTO().stream()
                .map(ProductIdQtyDTO::productId)
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

        User user = userService.getCurrentUser();

        Order order = Order.builder()
                .user(user)
                .shop(products.get(0).getShop())
                .userOrderInfo(request.userInfo())
                .status(OrderStatus.NEW)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (ProductIdQtyDTO reqItem : request.itemsDTO()) {
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

            total = total.add(
                    p.getPrice().multiply(BigDecimal.valueOf(reqItem.quantity()))
            );
        }

        order.setTotalPrice(total);

        return repository.save(order);
    }

    public OrderDTO toDTO(Order order) {
        return OrderDTO.builder()
                .itemsDTO(orderItemService.toItemDTO(order.getItems()))
                .userInfo(order.getStatus().equals(OrderStatus.NEW) ? null : order.getUserOrderInfo())
                .build();
    }

    public List<OrderDTO> toDTO(List<Order> orders) {
        return orders.stream()
                .map(this::toDTO)
                .toList();
    }




    @Transactional
    public void cancelOrder(Long orderId) {
        User user = userService.getCurrentUser();

        Order order = repository.findByIdAndUser(orderId, user)
                .orElseThrow(() ->
                        new OrderNotExistsException("Order with id: " + orderId + " was not found")
                );

        validateStatusChange(order.getStatus(), OrderStatus.CUSTOMER_CANCELED, Role.ROLE_USER);

        order.setStatus(OrderStatus.CUSTOMER_CANCELED);
    }

    public static void validateStatusChange (
            OrderStatus oldStatus,
            OrderStatus newStatus,
            Role actor
    ) {
        if (oldStatus == newStatus) {
            throw new IllegalStateException("Order already has status " + oldStatus);
        }

        if (oldStatus.isFinal()) {
            throw new IllegalStateException("Order is already finished");
        }

        Set<OrderStatus> allowed =
                ALLOWED_TRANSITIONS
                        .getOrDefault(actor, Map.of())
                        .getOrDefault(oldStatus, Set.of());

        if (!allowed.contains(newStatus)) {
            throw new IllegalStateException(
                    "Cannot change order status from " + oldStatus +
                            " to " + newStatus + " by " + actor
            );
        }
    }

    private static final Map<Role, Map<OrderStatus, Set<OrderStatus>>> ALLOWED_TRANSITIONS =
            Map.of(
                    Role.ROLE_SHOP_OWNER, Map.of(
                            OrderStatus.NEW, Set.of(OrderStatus.ACCEPTED, OrderStatus.OWNER_CANCELED),
                            OrderStatus.ACCEPTED, Set.of(OrderStatus.OWNER_CANCELED, OrderStatus.PAID),
                            OrderStatus.PAID, Set.of(OrderStatus.OWNER_CANCELED)
                    ),
                    Role.ROLE_USER, Map.of(
                            OrderStatus.NEW, Set.of(OrderStatus.CUSTOMER_CANCELED),
                            OrderStatus.ACCEPTED, Set.of(OrderStatus.CUSTOMER_CANCELED),
                            OrderStatus.PAID, Set.of(OrderStatus.CUSTOMER_CANCELED)
                    ),
                    Role.ROLE_SYSTEM, Map.of(
                            OrderStatus.PAID, Set.of(OrderStatus.SHIPPED),
                            OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED)
                    )

            );

}
