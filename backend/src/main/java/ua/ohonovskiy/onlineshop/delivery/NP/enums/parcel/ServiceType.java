package ua.ohonovskiy.onlineshop.delivery.NP.enums.parcel;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ServiceType {

    DOORS_DOORS("DoorsDoors"),
    DOORS_WAREHOUSE("DoorsWarehouse"),
    WAREHOUSE_WAREHOUSE("WarehouseWarehouse"),
    WAREHOUSE_DOORS("WarehouseDoors");

    private final String apiValue;

    ServiceType(String apiValue) {
        this.apiValue = apiValue;
    }

    @JsonValue
    public String getApiValue() {
        return apiValue;
    }
}
