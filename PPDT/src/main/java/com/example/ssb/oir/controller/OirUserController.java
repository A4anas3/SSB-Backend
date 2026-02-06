package com.example.ssb.oir.controller;


import com.example.ssb.oir.dto.OirTestCardDto;
import com.example.ssb.oir.dto.OirTestDetailDto;
import com.example.ssb.oir.service.OirTestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oir/tests")
@AllArgsConstructor
public class OirUserController {

    private final OirTestService testService;

    @GetMapping
    public List<OirTestCardDto> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/{testId}")
    public OirTestDetailDto getTest(@PathVariable Long testId) {
        return testService.getTestById(testId);
    }
}
