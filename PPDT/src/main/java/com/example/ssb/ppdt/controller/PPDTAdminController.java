package com.example.ssb.ppdt.controller;

import com.example.ssb.ppdt.DTO.PPDTAdminImageResponse;
import com.example.ssb.ppdt.DTO.PPDTImageRequest;
import com.example.ssb.ppdt.DTO.PPDTSampleToggleRequest;
import com.example.ssb.ppdt.Entity.PPDTImage;
import com.example.ssb.ppdt.Service.PPDTImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/admin/ppdt")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PPDTAdminController {

    private final PPDTImageService imageService;

    /* =========================
       ADD IMAGE (ADMIN)
       ========================= */
    @PostMapping("/image")
    public PPDTImage addImage(@RequestBody PPDTImageRequest req) {
        return imageService.saveImage(req);
    }

    /* =========================
       UPDATE IMAGE (ADMIN)
       ========================= */
    @PutMapping("/image/{id}")
    public PPDTImage updateImage(
            @PathVariable Long id,
            @RequestBody PPDTImageRequest req
    ) {
        return imageService.updateImage(id, req);
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
    @PutMapping("/image/{id}/toggle-sample")
    public PPDTImage toggleSample(
            @PathVariable Long id,
            @RequestBody PPDTSampleToggleRequest req
    ) {
        return imageService.toggleSample(
                id,
                req.getAction(),
                req.getSampleStory()
        );
    }


    /* =========================
       VIEW ALL IMAGES (ADMIN)
       ========================= */
    @GetMapping("/images")
    public List<PPDTAdminImageResponse> getAllImages() {
        return imageService.getAllImagesForAdmin();
    }


}
