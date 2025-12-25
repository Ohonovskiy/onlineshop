package ua.ohonovskiy.onlineshop.delivery.NP.DTO.shared;

import lombok.Data;

import java.util.List;

@Data
public class NPResponse<T> {
    private boolean success;
    private List<String> errors;
    private List<String> warnings;
    private List<String> info;
    private T data;
}
