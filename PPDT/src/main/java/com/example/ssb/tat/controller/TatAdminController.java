package com.example.ssb.tat.controller;

import com.example.ssb.tat.dto.AdminTatTestDto;
import com.example.ssb.tat.entity.TatTest;
import com.example.ssb.tat.entity.TatImage;

import com.example.ssb.tat.service.TatTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /* ================= ADD IMAGE ================= */
    @PostMapping(value = "/{testId}/image")
    public TatImage addImage(
            @PathVariable Long testId,
            @RequestParam("image") MultipartFile image,
            @RequestParam("imageContext") String imageContext,
            @RequestParam(value = "expectedTheme", required = false) String expectedTheme,
            @RequestParam(value = "story", required = false) String story
    ) {
        return tatAdminService.addTatImage(testId, image, imageContext, expectedTheme, story);
    }

    /* ================= UPDATE IMAGE ================= */
    @PutMapping(value = "/images/{imageId}")
    public TatImage updateImage(
            @PathVariable Long imageId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "imageContext", required = false) String imageContext,
            @RequestParam(value = "expectedTheme", required = false) String expectedTheme,
            @RequestParam(value = "story", required = false) String story
    ) {
        return tatAdminService.updateTatImage(imageId, image, imageContext, expectedTheme, story);
    }

    /* ================= DELETE IMAGE ================= */
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        tatAdminService.deleteTatImage(imageId);
        return ResponseEntity.noContent().build();
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
