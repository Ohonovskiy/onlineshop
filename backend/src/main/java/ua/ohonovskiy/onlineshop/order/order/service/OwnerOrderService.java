package ua.ohonovskiy.onlineshop.order.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ohonovskiy.onlineshop.order.order.DTO.OrderDTO;
import ua.ohonovskiy.onlineshop.order.order.DTO.OrderStatusRequest;
import ua.ohonovskiy.onlineshop.order.order.enums.OrderStatus;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.order.order.repository.OrderRepository;
import ua.ohonovskiy.onlineshop.shared.exception.OrderNotExistsException;
import ua.ohonovskiy.onlineshop.shop.service.ShopService;
import ua.ohonovskiy.onlineshop.user.enums.Role;
import ua.ohonovskiy.onlineshop.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerOrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ShopService shopService;
    private final OrderService orderService;

    public List<Order> getAllForCurrentOwner() {
        return orderRepository.findAllByShop_Owner(userService.getCurrentUser());
    }

    public List<OrderDTO> getAllDTOForCurrentOwner() {
        return orderService.toDTO(orderRepository.findAllByShop_Owner(userService.getCurrentUser()));
    }

    public OrderDTO getByIdForCurrentOwner(Long id) {
        return orderService.toDTO(orderRepository.findByIdAndShop_Owner(id, userService.getCurrentUser())
                .orElseThrow(() -> new OrderNotExistsException("Order with id: "+id+" was not found")));
    }

    @Transactional
    public OrderDTO changeStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findByIdAndShop(orderId, shopService.getForCurrentUser())
                .orElseThrow(() -> new OrderNotExistsException("Order was not found"));

        OrderStatus newStatus = request.orderStatus();

        OrderService.validateStatusChange(order.getStatus(), newStatus, Role.ROLE_SHOP_OWNER);

        order.setStatus(newStatus);

        return orderService.toDTO(orderRepository.save(order));
    }
}
