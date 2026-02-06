package com.example.ssb.ppdt.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(
        name = "ppdt_images",
        uniqueConstraints = @UniqueConstraint(columnNames = "imageUrl")
)
@Data
public class PPDTImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String imageContext;


    private String guide;

    /* SAMPLE FLAG */
    private Boolean isSample = false;

    /* SAMPLE-ONLY DATA */
    private String action;

    @Column(columnDefinition = "TEXT")
    private String sampleStory;
}
