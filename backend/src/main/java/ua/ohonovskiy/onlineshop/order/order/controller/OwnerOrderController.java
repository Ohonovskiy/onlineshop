package ua.ohonovskiy.onlineshop.order.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.ohonovskiy.onlineshop.order.order.enums.OrderStatus;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.order.order.service.OwnerOrderService;

import java.util.List;

@RestController
@RequestMapping("/owner/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SHOP_OWNER')")
public class OwnerOrderController {

    private final OwnerOrderService ownerOrderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(ownerOrderService.getAllForCurrentOwner());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerOrderService.getByIdForCurrentOwner(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Order> changeStatus(@PathVariable Long id,
                                              @RequestBody OrderStatus status) {
        return ResponseEntity.ok(ownerOrderService.changeStatus(id, status));
    }
}
