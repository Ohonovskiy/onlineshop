package ua.ohonovskiy.onlineshop.delivery.NP.DTO.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MethodProperties {
    @JsonProperty("PayerType")
    private String PayerType;

    @JsonProperty("PaymentMethod")
    private String PaymentMethod;

    @JsonProperty("DateTime")
    private String DateTime;

    @JsonProperty("CargoType")
    private String CargoType;

    @JsonProperty("Weight")
    private String Weight;

    @JsonProperty("ServiceType")
    private String ServiceType;

    @JsonProperty("SeatsAmount")
    private String SeatsAmount;

    @JsonProperty("Description")
    private String Description;

    @JsonProperty("Cost")
    private String Cost;

    @JsonProperty("CitySender")
    private String CitySender;

    @JsonProperty("Sender")
    private String Sender;

    @JsonProperty("SenderAddress")
    private String SenderAddress;

    @JsonProperty("ContactSender")
    private String ContactSender;

    @JsonProperty("SendersPhone")
    private String SendersPhone;

    @JsonProperty("CityRecipient")
    private String CityRecipient;

    @JsonProperty("Recipient")
    private String Recipient;

    @JsonProperty("RecipientAddress")
    private String RecipientAddress;

    @JsonProperty("ContactRecipient")
    private String ContactRecipient;

    @JsonProperty("RecipientsPhone")
    private String RecipientsPhone;

    @JsonProperty("FindByString")
    private String FindByString;

    @JsonProperty("CityName")
    private String CityName;

    @JsonProperty("Limit")
    private String Limit;

    @JsonProperty("Page")
    private String Page;
}
