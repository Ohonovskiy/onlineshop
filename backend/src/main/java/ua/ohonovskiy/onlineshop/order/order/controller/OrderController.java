package ua.ohonovskiy.onlineshop.order.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ohonovskiy.onlineshop.order.order.DTO.OrderDTO;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.order.order.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderDTO request) {
        return ResponseEntity.ok(orderService.placeOrderForCurrentUser(request));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(orderService.getAllForCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getForCurrentUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
