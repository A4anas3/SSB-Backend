package com.example.ssb.tat.dto;

import lombok.Data;

@Data
public class AdminTatImageDto {

    private Long id; // null for create, required for patch
    private String imageUrl;
    private String imageContext;
    private String expectedTheme;
    private String story;
}
