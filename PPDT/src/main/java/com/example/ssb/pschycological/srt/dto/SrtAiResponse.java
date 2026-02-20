package com.example.ssb.pschycological.srt.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SrtAiResponse {
    private String overall;
    private List<SrtAiItem> items;
}