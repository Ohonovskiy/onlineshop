package ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.enums.PayerType;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.enums.PaymentMethod;

@Data
@Builder
public class PaymentInfo {

    @JsonProperty("PayerType")
    private PayerType payerType;

    @JsonProperty("PaymentMethod")
    private PaymentMethod paymentMethod;
}
