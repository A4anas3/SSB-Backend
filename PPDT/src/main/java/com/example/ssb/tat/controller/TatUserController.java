package com.example.ssb.tat.controller;

import com.example.ssb.tat.dto.TatTestCardDto;
import com.example.ssb.tat.dto.TatTestDto;
import com.example.ssb.tat.dto.TatTestSampleDto;
import com.example.ssb.tat.service.TatTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tat/tests")
@RequiredArgsConstructor
public class TatUserController {

    private final TatTestService tatTestService;

    @GetMapping
    public List<TatTestCardDto> getAllTests() {
        return tatTestService.getAllTests();
    }

    @GetMapping("/{testId}")
    public TatTestDto getTestById(@PathVariable Long testId) {
        return tatTestService.getTestById(testId);
    }
    @GetMapping("/sample/{testId}")
    public TatTestSampleDto getSample(@PathVariable Long testId) {
        return tatTestService.getSampleByTestId(testId);
    }
}
