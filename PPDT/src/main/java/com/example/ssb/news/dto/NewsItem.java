package com.example.ssb.news.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsItem {

    private String source;
    private String heading;
    private String summary;
    private String link;
    private String published;


}
