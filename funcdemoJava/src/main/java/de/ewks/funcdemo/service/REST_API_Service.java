package de.ewks.funcdemo.service;

import de.ewks.funcdemo.domain.API_Reponse;
import io.reactivex.Flowable;
import org.springframework.http.MediaType;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class REST_API_Service extends API_Service {
    private String adress;

    public REST_API_Service(String adress) {
        this.adress = adress;
    }

    @Override
    public Flowable<API_Reponse> getAPI_Response() {
        return sampleAsync(this.adress).map(num -> new API_Reponse(num.doubleValue()));
    }

    @Override
    public API_Reponse getAPI_Response_Normal() {
        Number value = sampleSync(this.adress);
        return new API_Reponse(value.doubleValue());
    }


    private Number sampleSync(String adress) {
        try{
            final RestTemplate springBootRestTemplate = new RestTemplate();
            @SuppressWarnings("unchecked") final Map<String, Object> result = springBootRestTemplate.getForObject(adress + "/metrics", Map.class);
            return (Number) result.get("heap.used");
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    private Flowable<Number> sampleAsync(String adress) {
        final WebClient client = WebClient.create(adress);
        return Flowable.fromPublisher(client.get()
                .uri("/metrics/").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class))
                .map(map -> (Number) map.get("heap.used"));
    }

}
