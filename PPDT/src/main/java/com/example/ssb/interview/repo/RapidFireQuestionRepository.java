package com.example.ssb.interview.repo;



import com.example.ssb.interview.entity.RapidFireQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RapidFireQuestionRepository extends MongoRepository<RapidFireQuestion, String> {
    List<RapidFireQuestion> findAllByOrderBySequenceNumberAsc();

}
