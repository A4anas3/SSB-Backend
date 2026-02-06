package com.example.ssb.gto.lecturette.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lecturettes")
public class Lecturette {

    @Id
    private String id;
    private  String url;

    private String title;

    private String introduction;

    private List<SubHeading> subHeadings;

    private String conclusion;
    private String category;
}
