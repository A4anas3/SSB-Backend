package com.example.ssb.ppdt.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ppdt_ai_analysis")
@Data
public class PPDTAIAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to PPDT attempt
    private Long attemptId;

    /* =========================
       CORE PERCEPTION
       ========================= */
    private Integer numberOfCharacters;
    private Boolean basicDetailsIdentified; // age / gender / mood combined

    private Float perceptionScore;

    /* =========================
       STORY QUALITY
       ========================= */
    private Boolean logicalAndComplete; // past-present-future + flow
    private Boolean positiveAndRealistic;

    private Float storyScore;

    /* =========================
       EXPRESSION
       ========================= */
    private Integer wordCount;

    private Float clarityScore;

    /* =========================
       OLQ SIGNALS (PPDT-LEVEL ONLY)
       ========================= */
    private Float officerLikeThinkingScore; // reasoning + initiative + leadership combined

    /* =========================
       FINAL PPDT RESULT
       ========================= */
    private Float overallPpdtScore;

    @Column(columnDefinition = "TEXT")
    private String feedback; // strengths + improvements in one

    private LocalDateTime createdAt = LocalDateTime.now();
}
