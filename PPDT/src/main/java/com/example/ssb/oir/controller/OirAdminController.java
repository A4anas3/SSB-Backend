package com.example.ssb.oir.controller;


import com.example.ssb.oir.dto.AdminOirTestCreateDto;
import com.example.ssb.oir.dto.AdminOirTestPatchFullDto;
import com.example.ssb.oir.dto.OirTestCardDto;
import com.example.ssb.oir.service.OirTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/oir/tests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class OirAdminController {

    private final OirTestService testService;

    @PostMapping
    public OirTestCardDto createTest(@RequestBody AdminOirTestCreateDto dto) {
        return testService.createTest(dto);
    }

    @PatchMapping("/{testId}/full")
    public ResponseEntity<Void> patchTestWithQuestions(
            @PathVariable Long testId,
            @RequestBody AdminOirTestPatchFullDto dto
    ) {
        testService.patchTestWithQuestions(testId, dto);
        return ResponseEntity.noContent().build(); // 204
    }


    @DeleteMapping("/{testId}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long testId) {
        testService.deleteTest(testId);
        return ResponseEntity.noContent().build();
    }
}
