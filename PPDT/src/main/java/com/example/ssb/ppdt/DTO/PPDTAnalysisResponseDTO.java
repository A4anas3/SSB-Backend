package com.example.ssb.ppdt.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PPDTAnalysisResponseDTO {

    /* =========================
       CORE PERCEPTION
       ========================= */
    private Integer numberOfCharacters;
    private Boolean basicDetailsIdentified;
    private Float perceptionScore;

    /* =========================
       STORY QUALITY
       ========================= */
    private Boolean logicalAndComplete;
    private Boolean positiveAndRealistic;
    private Float storyScore;

    /* =========================
       EXPRESSION
       ========================= */
    private Integer wordCount;
    private Float clarityScore;

    /* =========================
       OLQ SIGNAL
       ========================= */
    private Float officerLikeThinkingScore;

    /* =========================
       FINAL RESULT
       ========================= */
    private Float overallPpdtScore;
    private String feedback;
}
