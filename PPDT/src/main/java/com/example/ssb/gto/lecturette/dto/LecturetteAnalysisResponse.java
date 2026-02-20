package com.example.ssb.gto.lecturette.dto;


import com.example.ssb.gto.lecturette.Entity.Lecturette;
import lombok.Data;

@Data
public class LecturetteAnalysisResponse {

    private boolean valid;
    private String message;

    // AI result
    private String confidence;
    private String clarity;
    private String structure;
    private String suggestions;
    private Float score;
    private String overall;

    // 🔥 FULL LECTURETTE FROM DB
    private Lecturette lecturette;
}