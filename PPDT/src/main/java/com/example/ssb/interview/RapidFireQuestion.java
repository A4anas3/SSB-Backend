package com.example.ssb.interview;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.List;

@Data

@Document(collection = "rapid_fire_questions")
public class RapidFireQuestion {

    @Id
    private String id;

    private int sequenceNumber;   // Sequence 1, 2, 3...
    private String title;          // Work History, Education, Family etc.
    private String description;    // Sequence description

    private String mainQuestion;   // Long main question

    private List<String> questionFlow;   // a,b,c,d points

    private List<String> pointsToRemember; // tips

    private String sampleAnswer;   // example answer
    private String notes;          // warnings / extra info// EASY, MEDIUM, HARD


}

