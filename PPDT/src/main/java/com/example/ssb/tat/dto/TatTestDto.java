package com.example.ssb.tat.dto;


import lombok.Data;
import java.util.List;

@Data
public class TatTestDto {

    private Long testId;
    private String testName;
    private List<TatImageDto> images;
}
