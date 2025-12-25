package ua.ohonovskiy.onlineshop.delivery.NP.DTO.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Counterparty {
    @JsonProperty("Ref")
    private String ref;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("CounterpartyProperty")
    private String counterpartyProperty; // Sender/Recipient

    @JsonProperty("CounterpartyType")
    private String counterpartyType;     // PrivatePerson/Organization
}
