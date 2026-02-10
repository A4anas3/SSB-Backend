package com.example.ssb.tat.service;

import com.example.ssb.tat.dto.*;
import com.example.ssb.tat.entity.TatImage;
import com.example.ssb.tat.entity.TatTest;
import com.example.ssb.tat.repo.TatImageRepository;
import com.example.ssb.tat.repo.TatTestRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.Map;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TatTestService {

    private final TatTestRepository tatTestRepository;
    private final TatImageRepository tatImageRepository;
    private final Cloudinary cloudinary;

    /* =====================================================
       USER APIs
    ===================================================== */

    /* USER: LIST ALL TESTS */
    public List<TatTestCardDto> getAllTests() {
        return tatTestRepository.findAll()
                .stream()
                .map(test -> {
                    TatTestCardDto dto = new TatTestCardDto();
                    dto.setId(test.getId());
                    dto.setTestName(test.getTestName());
                    return dto;
                })
                .toList();
    }

    /* USER: GET TEST BY ID */
    public TatTestDto getTestById(Long testId) {

        TatTest test = tatTestRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TAT Test not found"));

        TatTestDto dto = new TatTestDto();
        dto.setTestId(test.getId());
        dto.setTestName(test.getTestName());

        dto.setImages(
                test.getImages().stream().map(img -> {
                    TatImageDto imgDto = new TatImageDto();
                    imgDto.setImageId(img.getId());
                    imgDto.setImageUrl(img.getImageUrl());
                    imgDto.setImageContext(img.getImageContext());
                    return imgDto;
                }).toList()
        );

        return dto;
    }

    /* USER: SAMPLE VIEW */
    public TatTestSampleDto getSampleByTestId(Long testId) {

        TatTest test = tatTestRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TAT Test not found"));

        TatTestSampleDto dto = new TatTestSampleDto();
        dto.setTestId(test.getId());
        dto.setTestName(test.getTestName());

        dto.setImages(
                test.getImages().stream().map(img -> {
                    TatImageSampleDto sampleDto = new TatImageSampleDto();
                    sampleDto.setImageId(img.getId());
                    sampleDto.setImageUrl(img.getImageUrl());
                    sampleDto.setExpectedTheme(img.getExpectedTheme());
                    sampleDto.setStory(img.getStory());
                    sampleDto.setImageContext(img.getImageContext());
                    return sampleDto;
                }).toList()
        );

        return dto;
    }

    /* =====================================================
       ADMIN APIs
    ===================================================== */

    /* ADMIN: CREATE TEST */
    /* ADMIN: CREATE TEST (Shell only) */
    public TatTest createTest(AdminTatTestDto dto) {
        TatTest test = TatTest.builder()
                .testName(dto.getTestName())
                .images(new ArrayList<>())
                .build();
        return tatTestRepository.save(test);
    }

    /* ADMIN: ADD IMAGE TO TEST */
    public TatImage addTatImage(Long testId, MultipartFile file, String imageContext, String expectedTheme, String story) {
        TatTest test = tatTestRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TatTest not found"));

        try {
            String imageUrl = uploadToCloudinary(file);
            
            TatImage image = TatImage.builder()
                    .imageUrl(imageUrl)
                    .imageContext(imageContext)
                    .expectedTheme(expectedTheme)
                    .story(story)
                    .test(test)
                    .build();
            
            return tatImageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    /* ADMIN: UPDATE IMAGE */
    public TatImage updateTatImage(Long imageId, MultipartFile file, String imageContext, String expectedTheme, String story) {
        TatImage image = tatImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("TatImage not found"));

        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = uploadToCloudinary(file);
                image.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }

        if (imageContext != null) image.setImageContext(imageContext);
        if (expectedTheme != null) image.setExpectedTheme(expectedTheme);
        if (story != null) image.setStory(story);

        return tatImageRepository.save(image);
    }

    /* ADMIN: DELETE IMAGE */
    public void deleteTatImage(Long imageId) {
        TatImage image = tatImageRepository.findById(imageId).orElse(null);
        if (image != null && image.getImageUrl() != null) {
            String imageUrl = image.getImageUrl();
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    String publicId = extractPublicId(imageUrl);
                    if (publicId != null) {
                        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                        log.info("Async delete TAT image: {} Result: {}", publicId, result);
                    }
                } catch (IOException e) {
                    log.error("Async delete failed for TAT image: {}", imageUrl, e);
                }
            });
        }
        tatImageRepository.deleteById(imageId);
    }

    private String uploadToCloudinary(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    /* ADMIN: PATCH TEST */
    @Transactional
    public void patchTest(Long testId, AdminTatTestDto dto) {
        TatTest test = tatTestRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TAT Test not found"));

        if (dto.getTestName() != null) {
            test.setTestName(dto.getTestName());
        }

        tatTestRepository.save(test);
    }

    /* ADMIN: DELETE TEST */
    @Transactional
    public void deleteTest(Long testId) {

        TatTest test = tatTestRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TAT Test not found"));

        if (test.getImages() != null) {
            test.getImages().forEach(img -> {
                String imageUrl = img.getImageUrl();
                java.util.concurrent.CompletableFuture.runAsync(() -> {
                    try {
                        String publicId = extractPublicId(imageUrl);
                        if (publicId != null) {
                            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                            log.info("Async delete TAT image (cascade): {} Result: {}", publicId, result);
                        }
                    } catch (IOException e) {
                         log.error("Async delete failed for TAT image (cascade): {}", imageUrl, e);
                    }
                });
            });
        }
        tatTestRepository.delete(test); // ✅ cascades properly
    }

    private String extractPublicId(String imageUrl) {
        try {
            int uploadIndex = imageUrl.indexOf("/upload/");
            if (uploadIndex == -1) return null;
            String path = imageUrl.substring(uploadIndex + 8);
            if (path.matches("^v\\d+/.*")) {
                int firstSlash = path.indexOf("/");
                if (firstSlash != -1) {
                    path = path.substring(firstSlash + 1);
                }
            }
            int lastDot = path.lastIndexOf(".");
            if (lastDot != -1) {
                path = path.substring(0, lastDot);
            }
            return path;
        } catch (Exception e) {
            return null;
        }
    }

}
