package com.example.ssb.oir.repo;

import com.example.ssb.oir.model.OirQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OirQuestionRepository extends JpaRepository<OirQuestion, Long> {

    List<OirQuestion> findByTestIdOrderById(Long testId);
}
