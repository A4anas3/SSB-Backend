package com.example.ssb.ppdt.DTO;

import lombok.Data;

@Data
public class PPDTFullImageResponse {

    private Long id;
    private String imageUrl;
    private String imageContext;
    private String guide;

    private Boolean isSample;
    private String action;
    private String sampleStory;
}