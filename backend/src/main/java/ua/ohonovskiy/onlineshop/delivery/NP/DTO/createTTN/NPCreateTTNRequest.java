package ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN;

import lombok.Builder;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.shared.MethodProperties;

@Builder
public record NPCreateTTNRequest(
        String apiKey,
        String modelName,
        String calledMethod,
        MethodProperties methodProperties
) {}
