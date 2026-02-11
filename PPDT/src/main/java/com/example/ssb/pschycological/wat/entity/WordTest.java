package com.example.ssb.pschycological.wat.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "wat_tests")
public class WordTest {

    @Id
    private String id;


    private String testName; // WAT Test 1

    private List<WordItem> words; // 60 words
}
