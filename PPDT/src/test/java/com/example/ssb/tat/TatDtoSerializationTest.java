package com.example.ssb.tat;

import com.example.ssb.tat.dto.TatStoryInput;
import com.example.ssb.tat.dto.TatStoryOutput;
import com.example.ssb.tat.dto.TatAnalysisResponse;
import com.example.ssb.tat.dto.TatSubmitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TAT DTOs — ensures correct JSON serialization
 * (especially camelCase ↔ snake_case mapping for FastAPI).
 */
class TatDtoSerializationTest {

    private final ObjectMapper mapper = new ObjectMapper();

    // ──────────────────────────────────────────────
    // TatStoryInput  (sent TO FastAPI)
    // ──────────────────────────────────────────────

    @Test
    @DisplayName("TatStoryInput serializes imageId as image_id for FastAPI")
    void storyInput_serializesToSnakeCase() throws Exception {
        TatStoryInput input = new TatStoryInput();
        input.setImageId(42L);
        input.setContext("A soldier helping villagers");
        input.setStory("He organized relief work.");

        String json = mapper.writeValueAsString(input);

        assertTrue(json.contains("\"image_id\""), "Must serialize as image_id, got: " + json);
        assertTrue(json.contains("\"context\""));
        assertTrue(json.contains("\"story\""));
    }

    @Test
    @DisplayName("TatStoryInput accepts imageId from frontend (via @JsonAlias)")
    void storyInput_deserializesFromCamelCase() throws Exception {
        String json = """
                {"imageId": 99, "context": "ctx", "story": "my story"}
                """;

        TatStoryInput input = mapper.readValue(json, TatStoryInput.class);
        assertEquals(99L, input.getImageId());
    }

    @Test
    @DisplayName("TatStoryInput deserializes image_id back to imageId")
    void storyInput_deserializesFromSnakeCase() throws Exception {
        String json = """
                {"image_id": 7, "context": "ctx", "story": "my story"}
                """;

        TatStoryInput input = mapper.readValue(json, TatStoryInput.class);

        assertEquals(7L, input.getImageId());
        assertEquals("ctx", input.getContext());
        assertEquals("my story", input.getStory());
    }

    // ──────────────────────────────────────────────
    // TatStoryOutput  (received FROM FastAPI)
    // ──────────────────────────────────────────────

    @Test
    @DisplayName("TatStoryOutput deserializes image_id from FastAPI response")
    void storyOutput_deserializesFromSnakeCase() throws Exception {
        String json = """
                {
                  "image_id": 3,
                  "story": "He led the team.",
                  "grade": "GOOD",
                  "improvement": "Add more detail"
                }
                """;

        TatStoryOutput output = mapper.readValue(json, TatStoryOutput.class);

        assertEquals(3L, output.getImageId());
        assertEquals("GOOD", output.getGrade());
        assertEquals("Add more detail", output.getImprovement());
    }

    @Test
    @DisplayName("TatStoryOutput serializes as imageId (camelCase) for frontend")
    void storyOutput_serializesToCamelCase() throws Exception {
        TatStoryOutput output = new TatStoryOutput();
        output.setImageId(5L);
        output.setStory("test");
        output.setGrade("EXCELLENT");
        output.setImprovement("none");
        output.setDbStory("reference story");

        String json = mapper.writeValueAsString(output);

        assertTrue(json.contains("\"imageId\""), "Must be camelCase for frontend, got: " + json);
        assertFalse(json.contains("\"image_id\""), "Must NOT be snake_case for frontend, got: " + json);
    }

    // ──────────────────────────────────────────────
    // TatAnalysisResponse  (full response)
    // ──────────────────────────────────────────────

    @Test
    @DisplayName("Full TatAnalysisResponse deserializes correctly from FastAPI JSON")
    void analysisResponse_fullDeserialization() throws Exception {
        String json = """
                {
                  "overall": "GOOD",
                  "items": [
                    {
                      "image_id": 1,
                      "story": "Story one",
                      "grade": "EXCELLENT",
                      "improvement": "Great work"
                    },
                    {
                      "image_id": 2,
                      "story": "Story two",
                      "grade": "AVERAGE",
                      "improvement": "Add more positivity"
                    }
                  ]
                }
                """;

        TatAnalysisResponse response = mapper.readValue(json, TatAnalysisResponse.class);

        assertEquals("GOOD", response.getOverall());
        assertNotNull(response.getItems());
        assertEquals(2, response.getItems().size());

        assertEquals(1L, response.getItems().get(0).getImageId());
        assertEquals("EXCELLENT", response.getItems().get(0).getGrade());

        assertEquals(2L, response.getItems().get(1).getImageId());
        assertEquals("AVERAGE", response.getItems().get(1).getGrade());
    }

    // ──────────────────────────────────────────────
    // TatSubmitRequest  (frontend → Spring Boot)
    // ──────────────────────────────────────────────

    @Test
    @DisplayName("TatSubmitRequest serializes correctly with image_id in nested answers")
    void submitRequest_serializesCorrectly() throws Exception {
        TatStoryInput a1 = new TatStoryInput();
        a1.setImageId(1L);
        a1.setStory("My first story");

        TatStoryInput a2 = new TatStoryInput();
        a2.setImageId(2L);
        a2.setStory("");  // empty story (edge case)

        TatSubmitRequest request = new TatSubmitRequest();
        request.setTestId(100L);
        request.setAnswers(List.of(a1, a2));

        String json = mapper.writeValueAsString(request);

        assertTrue(json.contains("\"testId\""));  // frontend sends camelCase
        assertTrue(json.contains("\"image_id\""));  // nested answers use snake_case
        assertTrue(json.contains("\"story\":\"\""));  // empty story is valid
    }

    @Test
    @DisplayName("Empty story should not be null")
    void emptyStory_isNotNull() {
        TatStoryInput input = new TatStoryInput();
        input.setImageId(1L);
        input.setStory("");

        assertNotNull(input.getStory());
        assertEquals("", input.getStory());
    }
}
