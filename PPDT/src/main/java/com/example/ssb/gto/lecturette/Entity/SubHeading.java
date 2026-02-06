package com.example.ssb.gto.lecturette.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubHeading {

    private String heading;

    private List<String> explanations;
}
