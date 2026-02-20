package com.example.ssb.tat.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TatStoryInput {
    @JsonProperty("image_id")   // serialize as image_id (for FastAPI)
    @JsonAlias("imageId")       // also accept imageId (from frontend)
    private Long imageId;
    private String context;
    private String story;
}
