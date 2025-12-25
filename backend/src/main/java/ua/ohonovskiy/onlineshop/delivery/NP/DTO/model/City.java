package ua.ohonovskiy.onlineshop.delivery.NP.DTO.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class City {
    @JsonProperty("Ref")
    private String ref;

    @JsonProperty("Description")
    private String description;
}
