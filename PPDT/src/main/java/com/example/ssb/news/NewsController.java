package com.example.ssb.news;

import com.example.ssb.news.dto.NewsResponse;
import com.example.ssb.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public Mono<NewsResponse> fetchNews() {
        return newsService.getLatestNews();
    }
}
