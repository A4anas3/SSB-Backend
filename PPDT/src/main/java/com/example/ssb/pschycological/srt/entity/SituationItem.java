package com.example.ssb.pschycological.srt.entity;

import lombok.Data;

@Data
public class SituationItem {

    private int situationNo;       // 1–60
    private String situation;      // situation text
    private String reaction;       // candidate reaction / action
}
