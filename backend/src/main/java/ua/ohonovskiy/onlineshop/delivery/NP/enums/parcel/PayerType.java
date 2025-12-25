package ua.ohonovskiy.onlineshop.delivery.NP.enums.parcel;


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
