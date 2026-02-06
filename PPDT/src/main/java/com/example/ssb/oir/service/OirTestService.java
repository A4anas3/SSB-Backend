package com.example.ssb.oir.service;

import com.example.ssb.oir.dto.*;
import com.example.ssb.oir.model.*;
import com.example.ssb.oir.repo.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OirTestService {

    private final OirTestRepository testRepository;
    private final OirQuestionRepository questionRepository;

    // ---------------- USER ----------------

    public List<OirTestCardDto> getAllTests() {
        return testRepository.findAll()
                .stream()
                .map(t -> {
                    OirTestCardDto dto = new OirTestCardDto();
                    dto.setId(t.getId());
                    dto.setTestName(t.getTestName());
                    dto.setTotalQuestions(t.getTotalQuestions());
                    return dto;
                })
                .toList();
    }

    public OirTestDetailDto getTestById(Long testId) {

        OirTest test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        List<OirQuestionDto> questions =
                questionRepository.findByTestIdOrderById(testId)
                        .stream()
                        .map(q -> {
                            OirQuestionDto dto = new OirQuestionDto();
                            dto.setQuestionId(q.getId());
                            dto.setQuestionText(q.getQuestionText());
                            dto.setQuestionImage(q.getQuestionImage());
                            dto.setOptionA(q.getOptionA());
                            dto.setOptionB(q.getOptionB());
                            dto.setOptionC(q.getOptionC());
                            dto.setOptionD(q.getOptionD());
                            dto.setCorrectOption(q.getCorrectOption());
                            return dto;
                        })
                        .toList();

        OirTestDetailDto dto = new OirTestDetailDto();
        dto.setId(test.getId());
        dto.setTestName(test.getTestName());
        dto.setTotalQuestions(test.getTotalQuestions());
        dto.setQuestions(questions);

        return dto;
    }

    // ---------------- ADMIN ----------------

    @Transactional
    public OirTestCardDto createTest(AdminOirTestCreateDto dto) {

        if (dto.getQuestions() == null || dto.getQuestions().size() != 45) {
            throw new RuntimeException("OIR test must contain exactly 45 questions");
        }

        OirTest test = new OirTest();
        test.setTestName(dto.getTestName());
        OirTest savedTest = testRepository.save(test);

        for (AdminOirQuestionCreateDto q : dto.getQuestions()) {

            OirQuestion question = OirQuestion.builder()
                    .test(savedTest)
                    .questionText(q.getQuestionText())
                    .questionImage(q.getQuestionImage())
                    .optionA(q.getOptionA())
                    .optionB(q.getOptionB())
                    .optionC(q.getOptionC())
                    .optionD(q.getOptionD())
                    .correctOption(q.getCorrectOption())
                    .build();

            questionRepository.save(question);
        }

        OirTestCardDto res = new OirTestCardDto();
        res.setId(savedTest.getId());
        res.setTestName(savedTest.getTestName());
        res.setTotalQuestions(savedTest.getTotalQuestions());

        return res;
    }


    @Transactional
    public void patchTestWithQuestions(
            Long testId,
            AdminOirTestPatchFullDto dto
    ) {

        OirTest test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        // 1️⃣ Patch test name (if sent)
        if (dto.getTestName() != null) {
            test.setTestName(dto.getTestName());
        }

        // 2️⃣ Replace ALL questions (if sent)
        if (dto.getQuestions() != null) {

            if (dto.getQuestions().size() != 45) {
                throw new RuntimeException("OIR test must have exactly 45 questions");
            }

            // 🔥 IMPORTANT: orphanRemoval deletes old questions
            test.getQuestions().clear();

            for (AdminOirQuestionPatchDto q : dto.getQuestions()) {

                OirQuestion question = OirQuestion.builder()
                        .test(test)
                        .questionText(q.getQuestionText())
                        .questionImage(q.getQuestionImage())
                        .optionA(q.getOptionA())
                        .optionB(q.getOptionB())
                        .optionC(q.getOptionC())
                        .optionD(q.getOptionD())
                        .correctOption(q.getCorrectOption())
                        .build();

                test.getQuestions().add(question);
            }
        }

        testRepository.save(test);


    }

    public void deleteTest(Long testId) {
        if (!testRepository.existsById(testId)) {
            throw new RuntimeException("Test not found");
        }
        testRepository.deleteById(testId);
    }
}
