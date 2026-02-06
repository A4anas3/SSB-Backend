package com.example.ssb.gto.gpe.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "gpe")
public class Gpe {

    @Id
    private String id;
    private String imageUrl;
    private String question;

    private String overview;
    private boolean sample;

         // solution overview

    private List<GpePlan> plans;  // subheadings + explanation


}
