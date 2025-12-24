package ua.ohonovskiy.onlineshop.delivery.NP.DTO.shared;

import lombok.Builder;

@Builder
public record NPRequest(
        String apiKey,
        String modelName,
        String calledMethod,
        MethodProperties methodProperties
) {}
