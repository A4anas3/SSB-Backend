package com.example.ssb.pschycological.srt;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SrtTestService {

    private final SrtTestRepository repository;

    // ✅ Get test by ID
    public SrtTest getTestById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SRT Test not found with id: " + id));
    }

    // ✅ Create test
    public SrtTest createTest(SrtTest test) {
        return repository.save(test);
    }

    // ✅ Get all test names
    public List<TestNameDto> getAllTestNames() {
        return repository.findAll().stream().map(test -> {
            TestNameDto dto = new TestNameDto();
            dto.setId(test.getId());
            dto.setTestName(test.getTestName());
            return dto;
        }).toList();
    }

    // ✅ Delete test
    public void deleteTest(String id) {
        repository.deleteById(id);
    }

    public SrtTest updateFullTest(String id, SrtTest updatedTest) {

        SrtTest existingTest = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SRT Test not found with id: " + id));

        // ✅ Update test name
        if (updatedTest.getTestName() != null) {
            existingTest.setTestName(updatedTest.getTestName());
        }

        // ✅ Update all situations at once
        if (updatedTest.getSituations() != null && !updatedTest.getSituations().isEmpty()) {
            existingTest.setSituations(updatedTest.getSituations());
        }

        return repository.save(existingTest);
    }

}
