package com.example.ssb.pschycological.wat.dto;

import lombok.Data;
import java.util.List;

@Data
public class WatSubmitRequest {
    private String testId;
    private List<WatAnswerDto> answers;
}