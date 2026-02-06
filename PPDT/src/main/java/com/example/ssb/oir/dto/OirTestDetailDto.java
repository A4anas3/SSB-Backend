package com.example.ssb.oir.dto;

import lombok.Data;
import java.util.List;

@Data
public class OirTestDetailDto {
    private Long id;
    private String testName;
    private int totalQuestions;
    private List<OirQuestionDto> questions;
}
