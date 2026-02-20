package com.example.ssb.gto.lecturette.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ssb.gto.lecturette.Entity.Lecturette;
import com.example.ssb.gto.lecturette.dto.LecturetteAnalysisRequest;
import com.example.ssb.gto.lecturette.dto.LecturetteAnalysisResponse;
import com.example.ssb.gto.lecturette.dto.basic;
import com.example.ssb.gto.lecturette.repo.LecturetteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LecturetteService {
    private final WebClient webClient;

    @Value("${ai.lecturette.url}")
    private String aiUrl;

    @Value("${app.api-key}")
    private String apiKey;
    private final LecturetteRepository lecturetteRepository;
    private final Cloudinary cloudinary;

    public List<Lecturette> getAllLecturettes() {
        return lecturetteRepository.findAll();
    }

    public List<String> getDistinctCategories() {
        return lecturetteRepository.findAll()
                .stream()
                .map(Lecturette::getCategory)
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .sorted()
                .toList();
    }

    public Lecturette getLecturetteById(String id) {
        return lecturetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecturette not found with id: " + id));
    }

    public void createLecturette(Lecturette lecturette, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            try {
                String url = uploadToCloudinary(image);
                lecturette.setUrl(url);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }
        }
        lecturetteRepository.save(lecturette);
    }

    public Lecturette updateLecturette(String id, Lecturette lecturette) {
        Lecturette existing = lecturetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecturette not found with id: " + id));

        existing.setTitle(lecturette.getTitle());
        existing.setIntroduction(lecturette.getIntroduction());
        existing.setSubHeadings(lecturette.getSubHeadings());
        existing.setConclusion(lecturette.getConclusion());
        existing.setCategory(lecturette.getCategory());

        return lecturetteRepository.save(existing);
    }


    public void deleteLecturette(String id) {
        if (!lecturetteRepository.existsById(id)) {
            throw new RuntimeException("Lecturette not found with id: " + id);
        }
        lecturetteRepository.deleteById(id);
    }

    public Lecturette patchLecturette(String id, Lecturette lecturette, MultipartFile image) {
        Lecturette existing = lecturetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecturette not found with id: " + id));


        if (lecturette.getTitle() != null) {
            existing.setTitle(lecturette.getTitle());
        }
        if (lecturette.getIntroduction() != null) {
            existing.setIntroduction(lecturette.getIntroduction());
        }
        if (lecturette.getSubHeadings() != null) {
            existing.setSubHeadings(lecturette.getSubHeadings());
        }
        if (lecturette.getConclusion() != null) {
            existing.setConclusion(lecturette.getConclusion());
        }
        if (lecturette.getCategory() != null) {
            existing.setCategory(lecturette.getCategory());
        }

        if (image != null && !image.isEmpty()) {
            try {
                String url = uploadToCloudinary(image);
                existing.setUrl(url);
            } catch (IOException e) {
                 throw new RuntimeException("Image upload failed", e);
            }
        }


        return lecturetteRepository.save(existing);
    }
    public List<Lecturette> searchByTitle(String keyword) {
        return lecturetteRepository.findByTitleContainingIgnoreCase(keyword);
    }
    public List<Lecturette> searchByCategory(String category) {
        return lecturetteRepository.findByCategoryContainingIgnoreCase(category);
    }

    public List<basic> getBasicLecturettes() {
        return lecturetteRepository.findAll()
                .stream()
                .map(l -> new basic(
                        l.getId(),
                        l.getTitle(),
                        l.getUrl()
                ))
                .toList();
    }

    private String uploadToCloudinary(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "lecturette"));
        return uploadResult.get("url").toString();
    }

    public LecturetteAnalysisResponse analyse(LecturetteAnalysisRequest request) {

        LecturetteAnalysisResponse response = new LecturetteAnalysisResponse();

        // 🔹 get lecturette from DB
        Lecturette lecturette = lecturetteRepository.findById(request.getLecturetteId())
                .orElseThrow(() -> new RuntimeException("Lecturette not found"));

        response.setLecturette(lecturette);

        // ❌ duration validation
        if (request.getDurationSeconds() < 60) {
            response.setValid(false);
            response.setMessage("Please speak for at least 2 minutes before AI analysis.");
            return response;
        }

        // ❌ text validation
        if (request.getUserText() == null || request.getUserText().length() < 50) {
            response.setValid(false);
            response.setMessage("Please explain more. Minimum 50 characters required.");
            return response;
        }

        // 🧠 SEND ONLY WHAT FASTAPI EXPECTS
        Map<String, Object> payload = Map.of(
                "topic", lecturette.getTitle(),
                "userText", request.getUserText(),
                "durationSeconds", request.getDurationSeconds()
        );

        try {
            LecturetteAnalysisResponse aiResponse = webClient.post()
                    .uri(aiUrl)
                    .header("X-API-Key", apiKey)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(LecturetteAnalysisResponse.class)
                    .timeout(java.time.Duration.ofSeconds(60))
                    .block();

            if (aiResponse == null) {
                throw new RuntimeException("AI returned null response");
            }

            aiResponse.setValid(true);
            aiResponse.setLecturette(lecturette);
            return aiResponse;

        } catch (Exception e) {
            e.printStackTrace(); // print real error in console
            throw new RuntimeException("AI analysis failed: " + e.getMessage());
        }
    }
    public Lecturette getRandomLecturette() {
        List<Lecturette> all = lecturetteRepository.findAll();

        if (all.isEmpty()) {
            throw new RuntimeException("No lecturette topics found");
        }

        int index = (int) (Math.random() * all.size());
        return all.get(index);
    }

}
