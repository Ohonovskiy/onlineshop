package ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.enums.CargoType;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.enums.ServiceType;

@Data
@Builder
public class ParcelInfo {

    @JsonProperty("DateTime")
    private String dateTime; // dd.MM.yyyy

    @JsonProperty("CargoType")
    private CargoType cargoType;

    @JsonProperty("Weight")
    private String weight; // min 0.1

    @JsonProperty("ServiceType")
    private ServiceType serviceType;

    @JsonProperty("SeatsAmount")
    private String seatsAmount; // int, але віддаємо string

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Cost")
    private String cost; // int, але string
}

