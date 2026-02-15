package com.example.ssb.ppdt.Service;

import com.example.ssb.ppdt.Client.FastApiClient;
import com.example.ssb.ppdt.DTO.PPDTAnalysisResponseDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;



@Service
@RequiredArgsConstructor
public class PPDTService {

    private final FastApiClient fastApiClient;

    public PPDTAnalysisResponseDTO submit(
            String imageContext,
            String story,
            String action,
            String sampleStory
    ) {

        /* -------- validation -------- */
        if (story == null || story.trim().isEmpty()) {
            return invalid("Story cannot be empty");
        }

        if (story.length() < 30) {
            return invalid("Story too short");
        }

        /* -------- call FastAPI -------- */
        Map<String, Object> aiResult =
                fastApiClient.analyzePPDT(imageContext, story, action);

        if (aiResult == null || aiResult.isEmpty()) {
            throw new RuntimeException("AI analysis failed");
        }

        /* -------- map directly -------- */
        PPDTAnalysisResponseDTO dto = new PPDTAnalysisResponseDTO();

        dto.setStatus((String) aiResult.get("status"));

        Object scoreObj = aiResult.get("final_score");
        if (scoreObj != null) {
            dto.setFinalScore(Float.parseFloat(scoreObj.toString()));
        }

        dto.setOverallFeedback((String) aiResult.get("overall_feedback"));
        dto.setImprovements((String) aiResult.get("improvements"));
        dto.setMessage((String) aiResult.getOrDefault("message", "PPDT analysis complete"));
        dto.setSampleStory(sampleStory);

        return dto;
    }
    private PPDTAnalysisResponseDTO invalid(String msg) {
        PPDTAnalysisResponseDTO dto = new PPDTAnalysisResponseDTO();
        dto.setStatus("invalid");
        dto.setMessage(msg);
        dto.setFinalScore(0f);
        return dto;
    }
    /* =========================
       ENTITY → DTO MAPPER
       ========================= */

    private Integer getInt(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return v == null ? 0 : ((Number) v).intValue();
    }

    private Float getFloat(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return v == null ? 0.0f : ((Number) v).floatValue();
    }

}
