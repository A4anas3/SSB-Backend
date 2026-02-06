package com.example.ssb.oir.model;



import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "oir_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OirTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_name", nullable = false, unique = true)
    private String testName;

    @Column(name = "total_questions", nullable = false)
    private int totalQuestions = 45;

    @OneToMany(
            mappedBy = "test",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OirQuestion> questions;
}
