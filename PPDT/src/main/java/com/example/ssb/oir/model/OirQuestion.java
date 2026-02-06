package com.example.ssb.oir.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "oir_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OirQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    @JsonIgnore
    private OirTest test;





    @Column(name = "question_text", columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "question_image")
    private String questionImage;

    @Column(nullable = false)
    private String optionA;

    @Column(nullable = false)
    private String optionB;

    @Column(nullable = false)
    private String optionC;

    @Column(nullable = false)
    private String optionD;

    @Column(name = "correct_option", nullable = false)
    private String correctOption; // A / B / C / D
}
