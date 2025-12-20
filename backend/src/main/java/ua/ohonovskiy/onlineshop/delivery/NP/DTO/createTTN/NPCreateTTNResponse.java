package ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN;

import lombok.Data;

import java.util.List;

@Data
public class NPCreateTTNResponse {
    private boolean success;
    private List<Data> data;
    private List<String> errors;

    @lombok.Data
    public static class Data {
        private String Ref;
        private String wayBill;
    }
}