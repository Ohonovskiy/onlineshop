package ua.ohonovskiy.onlineshop.delivery.NP.enums.parcel;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CargoType {

    CARGO("Cargo"),
    PARCEL("Parcel"),
    DOCUMENTS("Documents");

    private final String apiValue;

    CargoType(String apiValue) {
        this.apiValue = apiValue;
    }

    @JsonValue
    public String getApiValue() {
        return apiValue;
    }
}
