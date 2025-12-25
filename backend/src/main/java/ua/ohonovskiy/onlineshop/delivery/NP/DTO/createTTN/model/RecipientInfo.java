package ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipientInfo {

    @JsonProperty("CityRecipient")
    private String cityRef;

    @JsonProperty("Recipient")
    private String recipientRef;

    @JsonProperty("RecipientAddress")
    private String addressRef;

    @JsonProperty("ContactRecipient")
    private String contactRef;

    @JsonProperty("RecipientsPhone")
    private String phone;
}

