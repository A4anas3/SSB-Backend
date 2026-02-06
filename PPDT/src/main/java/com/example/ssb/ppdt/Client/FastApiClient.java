package com.example.ssb.ppdt.Client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.HashMap;
import java.util.Map;

@Component
public class FastApiClient {

    private final WebClient webClient;

    @org.springframework.beans.factory.annotation.Value("${fastapi.analyse.url}")
    private String analyseUrl;

    public FastApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Map<String, Object> analyzePPDT(String imageContext, String storyText) {

        Map<String, String> request = new HashMap<>();
        request.put("image_context", imageContext);
        request.put("story_text", storyText);

        return webClient.post()
                .uri(analyseUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // ⚠️ Blocking for compatibility with PPDTService (JPA)
    }
}
