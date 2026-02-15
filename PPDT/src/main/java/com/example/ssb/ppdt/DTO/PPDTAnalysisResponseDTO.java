package com.example.ssb.ppdt.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PPDTAnalysisResponseDTO {

    // "valid" or "invalid"
    private String status;

    // 0–10 score
    private Float finalScore;

    private String overallFeedback;
    private String improvements;
    private String message;
    private String sampleStory;
}