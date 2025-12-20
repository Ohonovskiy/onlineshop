package ua.ohonovskiy.onlineshop.delivery.NP.model;

import jakarta.persistence.Entity;
import lombok.*;
import ua.ohonovskiy.onlineshop.delivery.shared.enums.DeliveryType;
import ua.ohonovskiy.onlineshop.delivery.shared.model.Parcel;

@Entity
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NPParcel extends Parcel {

    @Builder.Default
    private DeliveryType deliveryType = DeliveryType.NOVA_POSHTA;
}
