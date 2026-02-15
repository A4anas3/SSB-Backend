package com.example.ssb.ppdt.Service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ssb.ppdt.DTO.*;
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


    public PPDTImage saveImage(PPDTImageDTO dto) {

        try {
            String imageUrl = uploadToCloudinary(dto.getImage());

            PPDTImage entity = new PPDTImage();
            entity.setImageUrl(imageUrl);
            entity.setImageContext(dto.getImageContext());
            entity.setGuide(dto.getGuide());
            entity.setIsSample(dto.getIsSample() != null ? dto.getIsSample() : false);
            entity.setAction(dto.getAction());
            entity.setSampleStory(dto.getSampleStory());

            return imageRepo.save(entity);

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }


    public PPDTImage patchImage(Long id, PPDTImagePatchDTO dto) {

        PPDTImage image = imageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        try {

            /* ---------- IMAGE ---------- */
            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                String imageUrl = uploadToCloudinary(dto.getImage());
                image.setImageUrl(imageUrl);
            }

            /* ---------- TEXT FIELDS ---------- */
            if (dto.getImageContext() != null) {
                image.setImageContext(dto.getImageContext());
            }

            if (dto.getGuide() != null) {
                image.setGuide(dto.getGuide());
            }

            /* ---------- SAMPLE FLAG ---------- */
            if (dto.getIsSample() != null) {
                image.setIsSample(dto.getIsSample());
            }

            /* ---------- SAMPLE DATA ---------- */
            if (dto.getAction() != null) {
                image.setAction(dto.getAction());
            }

            if (dto.getSampleStory() != null) {
                image.setSampleStory(dto.getSampleStory());
            }

            return imageRepo.save(image);

        } catch (IOException e) {
            throw new RuntimeException("Patch failed: " + e.getMessage());
        }
    }

    private String uploadToCloudinary(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "ppdt"));
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





    public PPDTImage toggleSample(Long id) {

        PPDTImage image = imageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        Boolean current = image.getIsSample() != null ? image.getIsSample() : false;

        image.setIsSample(!current); // 🔥 toggle

        return imageRepo.save(image);
    }


    public List<PPDTFullImageResponse> getAllImages() {

        return imageRepo.findAll()
                .stream()
                .map(img -> {
                    PPDTFullImageResponse res = new PPDTFullImageResponse();

                    res.setId(img.getId());
                    res.setImageUrl(img.getImageUrl());
                    res.setImageContext(img.getImageContext());
                    res.setGuide(img.getGuide());
                    res.setIsSample(img.getIsSample());
                    res.setAction(img.getAction());
                    res.setSampleStory(img.getSampleStory());

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
    public PPDTFullImageResponse getFullImageById(Long id) {
        PPDTImage img = imageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        PPDTFullImageResponse res = new PPDTFullImageResponse();
        res.setId(img.getId());
        res.setImageUrl(img.getImageUrl());
        res.setImageContext(img.getImageContext());
        res.setGuide(img.getGuide());
        res.setIsSample(img.getIsSample());
        res.setAction(img.getAction());
        res.setSampleStory(img.getSampleStory());

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
