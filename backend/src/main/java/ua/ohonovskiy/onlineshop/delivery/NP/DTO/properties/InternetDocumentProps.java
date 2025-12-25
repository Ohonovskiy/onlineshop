package ua.ohonovskiy.onlineshop.delivery.NP.DTO.properties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InternetDocumentProps {
    private String PayerType;        // Sender/Recipient/ThirdPerson
    private String PaymentMethod;    // Cash/NonCash
    private String DateTime;         // dd.MM.yyyy
    private String CargoType;        // Cargo/Parcel/Documents...

    private String Weight;           // "0.5"
    private String ServiceType;      // WarehouseWarehouse etc
    private String SeatsAmount;      // "1"
    private String Description;      // text
    private String Cost;             // "500"

    private String CitySender;
    private String Sender;
    private String SenderAddress;
    private String ContactSender;
    private String SendersPhone;

    private String CityRecipient;
    private String Recipient;
    private String RecipientAddress;
    private String ContactRecipient;
    private String RecipientsPhone;
}
