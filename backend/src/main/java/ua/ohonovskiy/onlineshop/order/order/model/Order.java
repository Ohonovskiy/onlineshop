package ua.ohonovskiy.onlineshop.order.order.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.delivery.NP.model.NPParcel;
import ua.ohonovskiy.onlineshop.delivery.shared.model.Parcel;
import ua.ohonovskiy.onlineshop.order.order.enums.OrderStatus;
import ua.ohonovskiy.onlineshop.order.orderItem.model.OrderItem;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.user.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(exclude = {"user", "shop"})
@JsonIgnoreProperties({"user", "shop"})
public class Order extends SimpleEntityModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.NEW;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parcel_id")
    private Parcel parcel;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne
    private UserOrderInfo userOrderInfo;

    public void addItem(OrderItem item) {
        if (items == null) items = new ArrayList<>();
        items.add(item);
        item.setOrder(this);
    }

}

