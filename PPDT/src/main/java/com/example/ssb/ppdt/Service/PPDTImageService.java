package com.example.ssb.ppdt.Service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ssb.ppdt.DTO.PPDTAdminImageResponse;
import com.example.ssb.ppdt.DTO.PPDTSampleResponse;
import com.example.ssb.ppdt.DTO.PPDTTestImageResponse;
import com.example.ssb.ppdt.Entity.PPDTImage;
import com.example.ssb.ppdt.Repo.PPDTImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PPDTImageService {

    private final PPDTImageRepository imageRepo;
    private final Cloudinary cloudinary;

    /* =========================
       ADMIN: ADD IMAGE
       (always normal image)
       ========================= */
    /* =========================
       ADMIN: ADD IMAGE
       (always normal image)
       ========================= */
    public PPDTImage saveImage(MultipartFile file, String imageContext, String guide) {

        try {
            String imageUrl = uploadToCloudinary(file);

            PPDTImage image = new PPDTImage();
            image.setImageUrl(imageUrl);
            image.setImageContext(imageContext);
            image.setGuide(guide);

            image.setIsSample(false);
            image.setAction(null);
            image.setSampleStory(null);

            return imageRepo.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }


    /* =========================
       ADMIN: UPDATE IMAGE
       ========================= */
    /* =========================
       ADMIN: UPDATE IMAGE
       ========================= */
    public PPDTImage updateImage(Long id, MultipartFile file, String imageContext, String guide) {

        PPDTImage image = imageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = uploadToCloudinary(file);
                image.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }

        if (imageContext != null) image.setImageContext(imageContext);
        if (guide != null) image.setGuide(guide);

        return imageRepo.save(image);
    }

    private String uploadToCloudinary(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    /* =========================
       ADMIN: DELETE IMAGE
       ========================= */
    public void deleteImage(Long id) {
        PPDTImage image = imageRepo.findById(id).orElse(null);
        if (image != null && image.getImageUrl() != null) {
            String imageUrl = image.getImageUrl();
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    String publicId = extractPublicId(imageUrl);
                    if (publicId != null) {
                        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                        log.info("Async delete PPDT image: {} Result: {}", publicId, result);
                    }
                } catch (IOException e) {
                    log.error("Async delete failed for PPDT image: {}", imageUrl, e);
                }
            });
        }
        imageRepo.deleteById(id);
    }


    public PPDTImage toggleSample(Long id, String action, String sampleStory) {

        PPDTImage image = imageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // If this image is already sample → just remove sample
        if (Boolean.TRUE.equals(image.getIsSample())) {
            image.setIsSample(false);
            image.setAction(null);
            image.setSampleStory(null);
            return imageRepo.save(image);
        }



        // Mark current as sample
        image.setIsSample(true);
        image.setAction(action);
        image.setSampleStory(sampleStory);

        return imageRepo.save(image);
    }


    public List<PPDTSampleResponse> getAllSamplePPDTs() {

        List<PPDTImage> samples = imageRepo.findAllByIsSampleTrue();

        if (samples.isEmpty()) {
            throw new RuntimeException("No sample PPDTs configured");
        }

        return samples.stream()
                .map(this::mapToSampleResponse)
                .toList();
    }

    /* =========================
       USER: TEST MODE
       (same images, limited data)
       ========================= */
    public List<PPDTTestImageResponse> getTestImages() {

        return imageRepo.findAllByIsSampleFalse()
                .stream()
                .map(img -> {
                    PPDTTestImageResponse res = new PPDTTestImageResponse();
                    res.setId(img.getId());
                    res.setImageUrl(img.getImageUrl());
                    return res;
                })
                .toList();
    }


    /* =========================
       INTERNAL USE
       ========================= */
    public PPDTImage getById(Long id) {
        return imageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid image ID"));
    }
    public List<PPDTAdminImageResponse> getAllImagesForAdmin() {

        return imageRepo.findAll()
                .stream()
                .map(img -> {
                    PPDTAdminImageResponse res = new PPDTAdminImageResponse();
                    res.setId(img.getId());
                    res.setImageUrl(img.getImageUrl());
                    res.setIsSample(img.getIsSample());
                    res.setGuide(img.getGuide());
                    return res;
                })
                .toList();
    }

    private PPDTSampleResponse mapToSampleResponse(PPDTImage img) {

        if (!Boolean.TRUE.equals(img.getIsSample())) {
            throw new RuntimeException("Requested image is not a sample");
        }

        PPDTSampleResponse res = new PPDTSampleResponse();
        res.setId(img.getId());
        res.setImageUrl(img.getImageUrl());
        res.setImageContext(img.getImageContext());
        res.setAction(img.getAction());
        res.setSampleStory(img.getSampleStory());
        res.setGuide(img.getGuide());

        return res;
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
