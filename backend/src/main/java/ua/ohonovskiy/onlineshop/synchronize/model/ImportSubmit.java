package ua.ohonovskiy.onlineshop.synchronize.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.*;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.synchronize.enums.ImportStatus;
import ua.ohonovskiy.onlineshop.util.model.SimpleEntityModel;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportSubmit extends SimpleEntityModel {

    @OneToOne
    private Shop shop;

    @Enumerated(value = EnumType.STRING)
    private ImportStatus importStatus = ImportStatus.STATUS_ACCEPTED;
}
