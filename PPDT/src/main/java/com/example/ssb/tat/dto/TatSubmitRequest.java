package com.example.ssb.tat.dto;

import lombok.Data;
import java.util.List;

@Data
public class TatSubmitRequest {
    private Long testId;
    private List<TatStoryInput> answers;
}
