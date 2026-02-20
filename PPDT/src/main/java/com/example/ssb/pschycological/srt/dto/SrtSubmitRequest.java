package com.example.ssb.pschycological.srt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SrtSubmitRequest {

    private String testId;                  // which SRT set
    private int durationSeconds;            // optional
    private List<SrtAnswerDto> answers;     // 60 answers
}