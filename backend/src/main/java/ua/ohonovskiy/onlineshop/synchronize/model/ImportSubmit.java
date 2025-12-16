package ua.ohonovskiy.onlineshop.synchronize.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.synchronize.enums.ImportStatus;

import java.time.Instant;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(exclude = {"shop"})
@JsonIgnoreProperties({"shop"})
public class ImportSubmit extends SimpleEntityModel {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportStatus importStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Shop shop;

    @Column(length = 2000)
    private String errorMessage;

    private Instant startedAt;
    private Instant finishedAt;
}
