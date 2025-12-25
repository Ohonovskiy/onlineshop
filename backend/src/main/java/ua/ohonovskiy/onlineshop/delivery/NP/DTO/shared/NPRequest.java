package ua.ohonovskiy.onlineshop.delivery.NP.DTO.shared;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NPRequest {
    private String apiKey;
    private String modelName;
    private String calledMethod;
    private Object methodProperties;
}
