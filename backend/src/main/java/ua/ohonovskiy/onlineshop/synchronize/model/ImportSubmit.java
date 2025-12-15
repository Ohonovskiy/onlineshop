package ua.ohonovskiy.onlineshop.synchronize.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ua.ohonovskiy.onlineshop.shop.model.Shop;
import ua.ohonovskiy.onlineshop.synchronize.enums.ImportStatus;
import ua.ohonovskiy.onlineshop.shared.model.SimpleEntityModel;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportSubmit extends SimpleEntityModel {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportStatus importStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Shop shop;

    @Column(length = 2000)
    private String errorMessage;

    private Instant startedAt;
    private Instant finishedAt;
}
