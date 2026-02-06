package com.example.ssb.tat.service;

import com.example.ssb.tat.dto.*;
import com.example.ssb.tat.entity.TatImage;
import com.example.ssb.tat.entity.TatTest;
import com.example.ssb.tat.repo.TatTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TatTestService {

    private final TatTestRepository tatTestRepository;

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
    public TatTest createTest(AdminTatTestDto dto) {

        TatTest test = TatTest.builder()
                .testName(dto.getTestName())
                .build();

        List<TatImage> images = dto.getImages().stream()
                .map(imgDto -> TatImage.builder()
                        .imageUrl(imgDto.getImageUrl())
                        .imageContext(imgDto.getImageContext())
                        .expectedTheme(imgDto.getExpectedTheme())
                        .story(imgDto.getStory())
                        .test(test)
                        .build()
                ).toList();

        test.setImages(images);

        return tatTestRepository.save(test);
    }

    /* ADMIN: PATCH TEST */
    @Transactional
    public void patchTest(Long testId, AdminTatTestDto dto) {

        TatTest test = tatTestRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TAT Test not found"));

        if (dto.getTestName() != null) {
            test.setTestName(dto.getTestName());
        }

        if (dto.getImages() != null) {
            test.getImages().clear();

            dto.getImages().forEach(imgDto -> {
                TatImage image = TatImage.builder()
                        .imageUrl(imgDto.getImageUrl())
                        .imageContext(imgDto.getImageContext())
                        .expectedTheme(imgDto.getExpectedTheme())
                        .story(imgDto.getStory())
                        .test(test)
                        .build();

                test.getImages().add(image);
            });
        }

        tatTestRepository.save(test);
    }

    /* ADMIN: DELETE TEST */
    @Transactional
    public void deleteTest(Long testId) {

        TatTest test = tatTestRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("TAT Test not found"));

        tatTestRepository.delete(test); // ✅ cascades properly
    }

}
