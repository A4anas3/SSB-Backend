package com.example.ssb.tat.dto;


import lombok.Data;
import java.util.List;

@Data
public class AdminTatTestDto {

    private String testName;
    private List<AdminTatImageDto> images;
}
