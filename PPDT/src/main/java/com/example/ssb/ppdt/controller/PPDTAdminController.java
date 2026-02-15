package com.example.ssb.ppdt.controller;

import com.example.ssb.ppdt.DTO.*;

import com.example.ssb.ppdt.Entity.PPDTImage;
import com.example.ssb.ppdt.Service.PPDTImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.util.List;
@RestController
@RequestMapping("/admin/ppdt")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PPDTAdminController {

    private final PPDTImageService imageService;

    @PostMapping(value = "/image", consumes = "multipart/form-data")
    public ResponseEntity<String> addImage(@ModelAttribute PPDTImageDTO dto) {
        imageService.saveImage(dto);
        return ResponseEntity.ok("Image Added Successfully");
    }

    /* =========================
       UPDATE IMAGE (ADMIN)
       ========================= */
    @PatchMapping(value = "/image/{id}", consumes = "multipart/form-data")
    public ResponseEntity<String> patchImage(
            @PathVariable Long id,
            @ModelAttribute PPDTImagePatchDTO dto
    ) {
        imageService.patchImage(id, dto);
        return ResponseEntity.ok("Image Updated Successfully");
    }

    /* =========================
       DELETE IMAGE (ADMIN)
       ========================= */
    @DeleteMapping("/image/{id}")
    public void deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
    }

    /* =========================
       TOGGLE SAMPLE (ADMIN)
       ========================= */
    @PatchMapping("/image/{id}/toggle-sample")
    public ResponseEntity<String> toggleSample(@PathVariable Long id) {
        imageService.toggleSample(id);
        return ResponseEntity.ok("Sample Toggled Successfully");
    }


    /* =========================
       VIEW SINGLE IMAGE (ADMIN)
       ========================= */
    @GetMapping("/image/{id}")
    public PPDTFullImageResponse getAdminImageById(@PathVariable Long id) {
        return imageService.getFullImageById(id);
    }

    /* =========================
       VIEW ALL IMAGES (ADMIN)
       ========================= */
    @GetMapping("/images")
    public List<PPDTAdminImageResponse> getAllImages() {
        return imageService.getAllImagesForAdmin();
    }


}
