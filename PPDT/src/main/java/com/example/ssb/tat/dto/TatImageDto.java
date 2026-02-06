package com.example.ssb.tat.dto;

import lombok.Data;

@Data
public class TatImageDto {

    private Long imageId;

    /* Shown in frontend */
    private String imageUrl;

    /* Context for thinking / AI */
    private String imageContext;
}
