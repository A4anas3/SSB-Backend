package com.example.ssb.tat.controller;

import com.example.ssb.tat.dto.AdminTatTestDto;
import com.example.ssb.tat.entity.TatTest;

import com.example.ssb.tat.service.TatTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/tat/tests")
@RequiredArgsConstructor

@PreAuthorize("hasRole('ADMIN')")
public class TatAdminController {

    private final TatTestService tatAdminService;

    /* ================= CREATE ================= */
    @PostMapping
    public TatTest createTest(@RequestBody AdminTatTestDto dto) {
        return tatAdminService.createTest(dto);
    }

    /* ================= PATCH ================= */
    @PatchMapping("/{testId}")
    public ResponseEntity<Void> patchTest(
            @PathVariable Long testId,
            @RequestBody AdminTatTestDto dto
    ) {
        tatAdminService.patchTest(testId, dto);
        return ResponseEntity.noContent().build();
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{testId}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long testId) {
        tatAdminService.deleteTest(testId);
        return ResponseEntity.noContent().build();
    }
}
