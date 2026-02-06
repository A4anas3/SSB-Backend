package com.example.ssb.ppdt.Service;

import com.example.ssb.ppdt.Client.FastApiClient;
import com.example.ssb.ppdt.DTO.PPDTAnalysisResponseDTO;
import com.example.ssb.ppdt.Entity.PPDTAIAnalysis;
import com.example.ssb.ppdt.Entity.PPDTAttempt;
import com.example.ssb.ppdt.Repo.PPDTAIAnalysisRepository;
import com.example.ssb.ppdt.Repo.PPDTAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PPDTService {

    private final PPDTAttemptRepository attemptRepo;
    private final PPDTAIAnalysisRepository analysisRepo;
    private final FastApiClient fastApiClient;

    public PPDTAnalysisResponseDTO submit(
            Long imageId,
            String imageContext,
            String story,
            String action
    ) {

        /* 1️⃣ Save attempt */
        PPDTAttempt attempt = new PPDTAttempt();
        attempt.setImageId(imageId);
        attempt.setStoryText(story);
        attempt.setAction(action);
        attemptRepo.save(attempt);

        /* 2️⃣ Call FastAPI */
        Map<String, Object> aiResult =
                fastApiClient.analyzePPDT(imageContext, story);

        if (aiResult == null || aiResult.isEmpty()) {
            throw new RuntimeException("PPDT AI analysis failed");
        }

        /* 3️⃣ Save entity */
        PPDTAIAnalysis analysis = new PPDTAIAnalysis();
        analysis.setAttemptId(attempt.getId());

        analysis.setNumberOfCharacters(getInt(aiResult, "number_of_characters"));
        analysis.setBasicDetailsIdentified(
                Boolean.TRUE.equals(aiResult.get("basic_details_identified"))
        );
        analysis.setPerceptionScore(getFloat(aiResult, "perception_score"));

        analysis.setLogicalAndComplete(
                Boolean.TRUE.equals(aiResult.get("logical_and_complete"))
        );
        analysis.setPositiveAndRealistic(
                Boolean.TRUE.equals(aiResult.get("positive_and_realistic"))
        );
        analysis.setStoryScore(getFloat(aiResult, "story_score"));

        analysis.setWordCount(getInt(aiResult, "word_count"));
        analysis.setClarityScore(getFloat(aiResult, "clarity_score"));

        analysis.setOfficerLikeThinkingScore(
                getFloat(aiResult, "officer_like_thinking_score")
        );

        analysis.setOverallPpdtScore(
                getFloat(aiResult, "overall_ppdt_score")
        );

        analysis.setFeedback(
                (String) aiResult.getOrDefault(
                        "feedback",
                        "Good attempt. Improve clarity and structure."
                )
        );

        analysisRepo.save(analysis);

        /* 4️⃣ Return DTO */
        return mapToDTO(analysis);
    }

    /* =========================
       ENTITY → DTO MAPPER
       ========================= */
    private PPDTAnalysisResponseDTO mapToDTO(PPDTAIAnalysis a) {

        PPDTAnalysisResponseDTO dto = new PPDTAnalysisResponseDTO();

        dto.setNumberOfCharacters(a.getNumberOfCharacters());
        dto.setBasicDetailsIdentified(a.getBasicDetailsIdentified());
        dto.setPerceptionScore(a.getPerceptionScore());

        dto.setLogicalAndComplete(a.getLogicalAndComplete());
        dto.setPositiveAndRealistic(a.getPositiveAndRealistic());
        dto.setStoryScore(a.getStoryScore());

        dto.setWordCount(a.getWordCount());
        dto.setClarityScore(a.getClarityScore());

        dto.setOfficerLikeThinkingScore(
                a.getOfficerLikeThinkingScore()
        );

        dto.setOverallPpdtScore(a.getOverallPpdtScore());
        dto.setFeedback(a.getFeedback());

        return dto;
    }

    private Integer getInt(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return v == null ? 0 : ((Number) v).intValue();
    }

    private Float getFloat(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return v == null ? 0.0f : ((Number) v).floatValue();
    }

}
