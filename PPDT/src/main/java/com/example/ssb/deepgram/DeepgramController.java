package com.example.ssb.deepgram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deepgram")
public class DeepgramController {

    @Value("${deepgram.api-key}")
    private String apiKey;

    @Value("${deepgram.project-id}")
    private String projectId;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/token")
    public Map<String, String> getToken() {
        if (apiKey == null || apiKey.isEmpty() || projectId == null || projectId.isEmpty()) {
            throw new RuntimeException("Deepgram configuration is missing");
        }

        String url = "https://api.deepgram.com/v1/projects/" + projectId + "/keys";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request a temporary key valid for 300 seconds (5 minutes)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("comment", "Temporary frontend key");
        requestBody.put("scopes", List.of("usage:write"));
        requestBody.put("time_to_live_in_seconds", 300);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("key")) {
    Map<String, String> result = new HashMap<>();
    result.put("key", (String) responseBody.get("key"));
    return result;
}else {
                throw new RuntimeException("Deepgram API did not return an api_key");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate temporary Deepgram token", e);
        }
    }
}
