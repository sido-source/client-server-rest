package rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientService {

    @Autowired
    RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8081/api/v1";

    public void sendChange() {
        CryptoRequest cryptoRequest = CryptoRequest.builder()
                .val(20)
                .cryptoType(CryptoType.BTC)
                .indicator(CreditDebitIndicator.credit)  // <- ADDED THIS
                .build();

        restTemplate.postForLocation(baseUrl+ "/change", cryptoRequest);
    }

    public CryptoResponse getCurrent(CryptoType type) {
        return restTemplate.getForObject(baseUrl + "/{type}", CryptoResponse.class, type.name());
    }
}
