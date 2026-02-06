package com.example.ssb.tat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tat_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TatImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Image URL (real images only) */
    @Column(nullable = false)
    private String imageUrl;

    /* Context for AI (future use) */
    @Column(length = 2000)
    private String imageContext;

    /* Reference / expected theme (optional) */
    private String expectedTheme;

    /* Sample / reference story (optional) */
    @Column(length = 6000)
    private String story;



    /* 🔥 FIX: Prevent infinite JSON loop */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    @JsonIgnore
    private TatTest test;
}
