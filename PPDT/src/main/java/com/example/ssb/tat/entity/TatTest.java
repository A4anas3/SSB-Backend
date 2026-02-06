package com.example.ssb.tat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tat_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TatTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String testName;

    @OneToMany(
            mappedBy = "test",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private List<TatImage> images;
}
