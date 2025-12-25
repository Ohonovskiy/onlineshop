package ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {

    CASH("Cash"),
    NON_CASH("NonCash");

    private final String apiValue;

    PaymentMethod(String apiValue) {
        this.apiValue = apiValue;
    }

    @JsonValue
    public String getApiValue() {
        return apiValue;
    }
}
