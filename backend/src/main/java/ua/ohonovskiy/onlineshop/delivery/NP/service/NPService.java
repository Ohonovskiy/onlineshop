package ua.ohonovskiy.onlineshop.delivery.NP.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.NPCreateTTNRequest;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.NPCreateTTNResponse;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.shared.MethodProperties;

@Service
@RequiredArgsConstructor
public class NPService {
    @Value("${delivery.np.api.key}")
    private String API_KEY;

    @Value("${delivery.np.api.url}")
    private String API_URL;

    private final RestTemplate restTemplate = new RestTemplate();

    public NPCreateTTNResponse createInternetDocument(MethodProperties props) {
        NPCreateTTNRequest request = NPCreateTTNRequest.builder()
                .apiKey(API_KEY)
                .modelName("InternetDocument")
                .calledMethod("save")
                .methodProperties(props)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NPCreateTTNRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<NPCreateTTNResponse> response = restTemplate.postForEntity(
                API_URL,
                entity,
                NPCreateTTNResponse.class
        );

        NPCreateTTNResponse body = response.getBody();

        if (body == null || !body.isSuccess()) {
            throw new IllegalStateException(
                    "NP error: " + (body != null ? body.getErrors() : "empty response")
            );
        }

        return body;
    }
}
