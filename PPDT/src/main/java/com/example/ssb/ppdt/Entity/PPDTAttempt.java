package com.example.ssb.ppdt.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ppdt_attempts")
@Data
public class PPDTAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long imageId;

    @Lob
    private String storyText;
    private String action;

    private LocalDateTime submittedAt = LocalDateTime.now();
}
