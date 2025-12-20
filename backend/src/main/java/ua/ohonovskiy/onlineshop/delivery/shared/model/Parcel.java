package ua.ohonovskiy.onlineshop.delivery.shared.model;

import jakarta.persistence.*;
import ua.ohonovskiy.onlineshop.order.order.model.Order;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Parcel extends SimpleEntityModel {
     @Column(nullable = false, unique = true)
     private String wayBill;

     @OneToOne(mappedBy = "parcel")
     private Order order;
}
