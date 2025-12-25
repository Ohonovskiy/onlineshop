package ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum PayerType {

    SENDER("Sender"),
    RECIPIENT("Recipient"),
    THIRD_PERSON("ThirdPerson");

    private final String apiValue;

    PayerType(String apiValue) {
        this.apiValue = apiValue;
    }

    @JsonValue
    public String getApiValue() {
        return apiValue;
    }
}
