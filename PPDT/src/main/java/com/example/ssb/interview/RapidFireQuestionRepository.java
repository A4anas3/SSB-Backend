package com.example.ssb.interview;



import com.example.ssb.interview.RapidFireQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RapidFireQuestionRepository extends MongoRepository<RapidFireQuestion, String> {
    List<RapidFireQuestion> findAllByOrderBySequenceNumberAsc();

}
