package com.example.ssb.pschycological.wat.service;


import com.example.ssb.pschycological.wat.repo.WordTestRepository;
import com.example.ssb.pschycological.wat.dto.TestNameDto;
import com.example.ssb.pschycological.wat.entity.WordTest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordTestService {

    private final WordTestRepository repository;





    public WordTest getTestById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found with id: " + id));
    }


    public WordTest createTest(WordTest test) {
        return repository.save(test);
    }
    public List<TestNameDto> getAllTestNames() {
        return repository.findAll().stream().map(test -> {
            TestNameDto dto = new TestNameDto();
            dto.setId(test.getId());
            dto.setTestName(test.getTestName());
            return dto;
        }).toList();
    }


    public void deleteTest(String id) {
        repository.deleteById(id);
    }




    public WordTest updateFullTest(String id, WordTest updatedTest) {
        WordTest existing = getTestById(id);

        if (updatedTest.getTestName() != null) {
            existing.setTestName(updatedTest.getTestName());
        }

        if (updatedTest.getWords() != null) {
            existing.setWords(updatedTest.getWords());
        }

        return repository.save(existing);
    }


}

