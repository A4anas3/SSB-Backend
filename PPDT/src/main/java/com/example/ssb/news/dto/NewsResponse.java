package com.example.ssb.news.dto;

import lombok.Data;
import java.util.List;

@Data
public class NewsResponse {

    private String last_updated;
    private int count;
    private List<NewsItem> news;
}
