package com.example.ssb.tat.dto;

import lombok.Data;
import java.util.List;

@Data
public class TatAnalysisResponse {
    private String overall;
    private List<TatStoryOutput> items;
}
