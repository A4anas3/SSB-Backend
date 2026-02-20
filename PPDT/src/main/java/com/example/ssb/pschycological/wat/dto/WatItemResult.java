package com.example.ssb.pschycological.wat.dto;

import lombok.Data;

@Data
public class WatItemResult {
    private int wordNo;
    private String word;
    private String sentence;
    private String grade;
    private String improvement;
    private String improvedSentence;
}
