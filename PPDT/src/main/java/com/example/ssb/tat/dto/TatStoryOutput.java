package com.example.ssb.tat.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class TatStoryOutput {
    @JsonAlias("image_id")  // accept image_id from FastAPI, serialize as imageId for frontend
    private Long imageId;
    private String imgurl;
    private String story;
    private String grade;
    private String improvement;
    private String dbStory;
}
