package com.example.ssb.tat.dto;

import lombok.Data;

@Data
public class TatImageSampleDto {


    private Long imageId;
    private String imageUrl;
    private String imageContext;
    private String expectedTheme;
    private String story;
}
