package com.example.ssb.pschycological.wat.dto;

import lombok.Data;

import java.util.List;

@Data
public class WatSubmitResponse {
    private String overall;
    private List<WatItemResult> items;
}
