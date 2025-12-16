package ua.ohonovskiy.onlineshop.order.order.model;

import jakarta.persistence.Entity;
import lombok.*;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UserOrderInfo extends SimpleEntityModel {
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private String comment;
}
