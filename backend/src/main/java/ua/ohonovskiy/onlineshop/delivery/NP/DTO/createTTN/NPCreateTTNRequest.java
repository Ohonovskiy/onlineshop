package ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Data;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.model.ParcelInfo;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.model.PaymentInfo;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.model.RecipientInfo;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.model.SenderInfo;

@Data
@Builder
public class NPCreateTTNRequest {

    @JsonUnwrapped
    private PaymentInfo payment;

    @JsonUnwrapped
    private ParcelInfo parcel;

    @JsonUnwrapped
    private SenderInfo sender;

    @JsonUnwrapped
    private RecipientInfo recipient;
}
