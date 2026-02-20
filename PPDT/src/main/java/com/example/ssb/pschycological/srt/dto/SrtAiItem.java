package com.example.ssb.pschycological.srt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SrtAiItem {
    private int situationNo;
    private String situation;
    private String reaction;
    private String grade;
    private String improvement;
    private String possibleAnswer;
}