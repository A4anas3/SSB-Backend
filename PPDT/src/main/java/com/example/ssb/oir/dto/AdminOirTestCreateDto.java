package com.example.ssb.oir.dto;
import lombok.Data;
import java.util.List;

@Data
public class AdminOirTestCreateDto {

    private String testName;
    private List<AdminOirQuestionCreateDto> questions;
}
