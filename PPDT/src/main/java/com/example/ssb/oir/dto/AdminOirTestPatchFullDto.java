package com.example.ssb.oir.dto;



import lombok.Data;
import java.util.List;

@Data
public class AdminOirTestPatchFullDto {

    private String testName; // optional
    private List<AdminOirQuestionPatchDto> questions; // full replace
}
