package ua.ohonovskiy.onlineshop.delivery.NP.DTO.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Warehouse {
    @JsonProperty("Ref")
    private String ref;

    @JsonProperty("Number")
    private String number;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("CityRef")
    private String cityRef;
}
