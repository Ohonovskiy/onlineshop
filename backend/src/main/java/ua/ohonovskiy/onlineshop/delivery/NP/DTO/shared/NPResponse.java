package ua.ohonovskiy.onlineshop.delivery.NP.DTO.shared;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class NPResponse<T> {
    private boolean success;
    private List<String> errors;
    private List<String> warnings;
    private JsonNode info;
    private T data;
}
