package ua.ohonovskiy.onlineshop.delivery.NP.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.Settlement;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.createTTN.NPCreateTTNResponse;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.shared.MethodProperties;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.shared.NPRequest;
import ua.ohonovskiy.onlineshop.delivery.NP.DTO.shared.NPResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NPService {
    @Value("${delivery.np.api.key}")
    private String API_KEY;

    @Value("${delivery.np.api.url}")
    private String API_URL;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Settlement> getSettlements(MethodProperties props) {
        NPResponse<List<Settlement>> resp = sendRequest(
                "AddressGeneral",
                "getSettlements",
                props,
                new ParameterizedTypeReference<>() {}
        );
        return resp.getData();
    }

//    public List<Settlement> getSettlements(MethodProperties props) {
//        NPResponse<List<Settlement>> resp = sendRequest(
//                "AddressGeneral",
//                "getSettlements",
//                props,
//                new ParameterizedTypeReference<>() {}
//        );
//        return resp.getData();
//    }

    public NPCreateTTNResponse createWayBill(MethodProperties props) {
        NPResponse<List<NPCreateTTNResponse>> resp = sendRequest(
                "InternetDocument",
                "save",
                props,
                new ParameterizedTypeReference<>() {}
        );
        return resp.getData().get(0);
    }

    private <T> NPResponse<T> sendRequest(
            String modelName,
            String calledMethod,
            MethodProperties props,
            ParameterizedTypeReference<NPResponse<T>> typeRef
    ) {
        NPRequest request = NPRequest.builder()
                .apiKey(API_KEY)
                .modelName(modelName)
                .calledMethod(calledMethod)
                .methodProperties(props)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NPRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<NPResponse<T>> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                typeRef
        );

        NPResponse<T> body = response.getBody();
        if (body == null) throw new IllegalStateException("NP error: empty response");
        if (!body.isSuccess()) throw new IllegalStateException("NP error: " + body.getErrors());

        return body;
    }
}
