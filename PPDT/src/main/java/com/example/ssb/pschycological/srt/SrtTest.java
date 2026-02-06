package com.example.ssb.pschycological.srt;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "srt_tests")
public class SrtTest {

    @Id
    private String id;

    private String testName;                // SRT Test 1
    private List<SituationItem> situations; // 60 situations
}
