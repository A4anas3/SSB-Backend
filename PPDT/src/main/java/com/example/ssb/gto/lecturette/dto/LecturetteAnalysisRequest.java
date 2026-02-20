package com.example.ssb.gto.lecturette.dto;

import lombok.Data;

@Data
public class LecturetteAnalysisRequest {

    private String lecturetteId;   // used to fetch from DB
    private String topic;          // send to AI also
    private String userText;
    private int durationSeconds;
}