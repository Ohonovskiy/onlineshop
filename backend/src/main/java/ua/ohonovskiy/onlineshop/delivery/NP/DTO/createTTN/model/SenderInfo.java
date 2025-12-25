package ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SenderInfo {

    @JsonProperty("CitySender")
    private String cityRef;

    @JsonProperty("Sender")
    private String senderRef;

    @JsonProperty("SenderAddress")
    private String addressRef;

    @JsonProperty("ContactSender")
    private String contactRef;

    @JsonProperty("SendersPhone")
    private String phone;
}
