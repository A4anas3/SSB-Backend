package com.example.ssb.oir.dto;

import lombok.Data;

@Data
public class AdminOirQuestionCreateDto {

    private String questionText;
    private String questionImage;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctOption;
}
