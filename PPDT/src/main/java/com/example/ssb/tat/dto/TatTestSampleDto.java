package com.example.ssb.tat.dto;


import lombok.Data;
import java.util.List;

@Data
public class TatTestSampleDto {

    private Long testId;
    private String testName;
    private List<TatImageSampleDto> images;
}
