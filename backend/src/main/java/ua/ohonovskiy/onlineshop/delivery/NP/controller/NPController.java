package ua.ohonovskiy.onlineshop.delivery.NP.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.Settlement;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.NPCreateTTNResponse;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.shared.MethodProperties;
import ua.ohonovskiy.onlineshop.delivery.NP.service.NPService;

import java.util.List;

@RestController
@RequestMapping("/np")
@RequiredArgsConstructor
public class NPController {
    private final NPService npService;

    @GetMapping("/settlements")
    public ResponseEntity<List<Settlement>> getSettlements(@RequestParam String name) {
        return ResponseEntity.ok(
                npService.getSettlementsByName(
                        MethodProperties.builder().FindByString(name).build()
                )
        );
    }

    @PostMapping
    public ResponseEntity<NPCreateTTNResponse> createParcel() {
        return ResponseEntity.ok(
                npService.createWayBill(
                        MethodProperties.builder()
                                .PayerType("Recipient")
                                .PaymentMethod("Cash")
                                .DateTime("22.12.2025")
                                .CargoType("Parcel")
                                .ServiceType("WarehouseWarehouse")
                                .SeatsAmount("1")
                                .Weight("1")
                                .Cost("500")
                                .Description("Тестовий товар")

                                // sender
                                .CitySender("8d5a980d-391c-11dd-90d9-001a92567626")
                                .Sender("11111111-1111-1111-1111-111111111111")
                                .ContactSender("2182da7a-cb15-11ee-a60f-48df37b921db")
                                .SendersPhone("380501234567")
                                .SenderAddress("33333333-3333-3333-3333-333333333333")

                                // recipient
                                .CityRecipient("db5c88f5-391c-11dd-90d9-001a92567626")
                                .Recipient("44444444-4444-4444-4444-444444444444")
                                .ContactRecipient("55555555-5555-5555-5555-555555555555")
                                .RecipientsPhone("380671234567")
                                .RecipientAddress("66666666-6666-6666-6666-666666666666")

                                .build()
                )
        );
    }

}
