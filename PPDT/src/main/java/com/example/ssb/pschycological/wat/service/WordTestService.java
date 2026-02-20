package com.example.ssb.pschycological.wat.service;


import com.example.ssb.pschycological.wat.dto.WatItemResult;
import com.example.ssb.pschycological.wat.dto.WatSubmitRequest;
import com.example.ssb.pschycological.wat.dto.WatSubmitResponse;
import com.example.ssb.pschycological.wat.repo.WordTestRepository;
import com.example.ssb.pschycological.wat.dto.TestNameDto;
import com.example.ssb.pschycological.wat.entity.WordTest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordTestService {

    private final WordTestRepository repository;

    private final WebClient webClient;

    @Value("${fastapi.wat}")
    private String watUrl;

    @Value("${app.api-key}")
    private String apiKey;



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


    public WatSubmitResponse submitTest(WatSubmitRequest request) {
        WordTest test = repository.findById(request.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        WatSubmitResponse aiResponse = webClient.post()
                .uri(watUrl)
                .header("X-API-Key", apiKey)
                .bodyValue(request.getAnswers())
                .retrieve()
                .bodyToMono(WatSubmitResponse.class)
                .timeout(java.time.Duration.ofSeconds(45))
                .block();

        // 3️⃣ Merge DB model answer into response
        for (WatItemResult item : aiResponse.getItems()) {

            test.getWords().stream()
                    .filter(w -> w.getWordNo() == item.getWordNo())
                    .findFirst()
                    .ifPresent(w -> {
                        item.setImprovedSentence(w.getSentence());
                    });
        }

        return aiResponse;
    }
}

