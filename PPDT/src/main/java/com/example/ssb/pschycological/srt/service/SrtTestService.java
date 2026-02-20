package com.example.ssb.pschycological.srt.service;


import com.example.ssb.pschycological.srt.dto.*;
import com.example.ssb.pschycological.srt.entity.SrtTest;
import com.example.ssb.pschycological.srt.repo.SrtTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SrtTestService {
    private final WebClient webClient;
    // 🔥 add this
    @Value("${fastapi.srt}")
    private String analyseUrl;

    @Value("${app.api-key}")
    private String apiKey;

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

    // 🔥 SUBMIT TEST → SEND TO AI
    public SrtAiResponse submitTest(SrtSubmitRequest request) {

        SrtTest test = repository.findById(request.getTestId())
                .orElseThrow(() -> new RuntimeException("SRT test not found"));

        // O(1) lookup map for situations
        var situationMap = test.getSituations().stream()
                .collect(java.util.stream.Collectors.toMap(
                        s -> s.getSituationNo(), s -> s));

        // Save DB answers + inject user reactions in one pass
        var dbAnswers = new java.util.HashMap<Integer, String>();
        for (SrtAnswerDto r : request.getAnswers()) {
            var s = situationMap.get(r.getSituationNo());
            if (s != null) {
                if (s.getReaction() != null && !s.getReaction().isBlank()) {
                    dbAnswers.put(s.getSituationNo(), s.getReaction());
                }
                s.setReaction(r.getReaction());
            }
        }

        // Call AI
        SrtAiResponse aiResponse = webClient.post()
                .uri(analyseUrl)
                .header("X-API-Key", apiKey)
                .bodyValue(test.getSituations())
                .retrieve()
                .bodyToMono(SrtAiResponse.class)
                .timeout(java.time.Duration.ofSeconds(60))
                .block();

        // Attach DB possibleAnswer
        aiResponse.getItems().forEach(item ->
                item.setPossibleAnswer(dbAnswers.getOrDefault(item.getSituationNo(), null))
        );

        return aiResponse;
    }


}
