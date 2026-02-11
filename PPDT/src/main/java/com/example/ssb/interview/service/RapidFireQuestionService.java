package com.example.ssb.interview.service;



import com.example.ssb.interview.entity.RapidFireQuestion;
import com.example.ssb.interview.repo.RapidFireQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RapidFireQuestionService {

    private final RapidFireQuestionRepository repository;

    // ✅ Get All
    public List<RapidFireQuestion> getAll() {
        return repository.findAllByOrderBySequenceNumberAsc();
    }


    // ✅ Get By ID
    public RapidFireQuestion getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapid Fire Question not found with id: " + id));
    }

    // ✅ Create
    public RapidFireQuestion create(RapidFireQuestion question) {
        return repository.save(question);
    }

    // ✅ Patch (Partial Update)
    public RapidFireQuestion patch(String id, RapidFireQuestion updated) {
        RapidFireQuestion existing = getById(id);

        if (updated.getSequenceNumber() != 0) existing.setSequenceNumber(updated.getSequenceNumber());
        if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getMainQuestion() != null) existing.setMainQuestion(updated.getMainQuestion());
        if (updated.getQuestionFlow() != null) existing.setQuestionFlow(updated.getQuestionFlow());
        if (updated.getPointsToRemember() != null) existing.setPointsToRemember(updated.getPointsToRemember());
        if (updated.getSampleAnswer() != null) existing.setSampleAnswer(updated.getSampleAnswer());
        if (updated.getNotes() != null) existing.setNotes(updated.getNotes());

        return repository.save(existing);
    }

    // ✅ Delete
    public void delete(String id) {
        repository.deleteById(id);
    }
}
