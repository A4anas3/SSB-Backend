package com.example.ssb.news.service;

import com.example.ssb.news.dto.NewsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NewsService {

    private final WebClient webClient;

    @Value("${fastapi.news.url}")
    private String fastApiUrl;

    public NewsService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<NewsResponse> getLatestNews() {
        return webClient.get()
                .uri(fastApiUrl)
                .retrieve()
                .bodyToMono(NewsResponse.class);
    }
}
