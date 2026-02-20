package com.example.ssb.ppdt.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class FastApiClient {

    private final WebClient webClient;

    @Value("${fastapi.analyse.url}")
    private String analyseUrl;

    @Value("${app.api-key}")
    private String apiKey;

    public FastApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Map<String, Object> analyzePPDT(String imageContext, String storyText, String action) {

        Map<String, Object> request = new HashMap<>();
        request.put("image_context", imageContext);
        request.put("story", storyText);
        request.put("action", action);
        request.put("age", 20);

        return webClient.post()
                .uri(analyseUrl)   // only path here
                .header("X-API-Key", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(java.time.Duration.ofSeconds(30))
                .block();
    }
}
