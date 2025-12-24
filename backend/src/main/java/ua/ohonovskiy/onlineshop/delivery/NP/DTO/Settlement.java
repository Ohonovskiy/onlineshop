package ua.ohonovskiy.onlineshop.delivery.NP.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Settlement {
    @JsonProperty("Ref")
    private String ref;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("SettlementTypeDescription")
    private String settlementTypeDescription;

    @JsonProperty("RegionsDescription")
    private String regionsDescription;

    @JsonProperty("AreaDescription")
    private String areaDescription;
}
