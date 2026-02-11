package com.example.ssb.gto.gpe.service;



import com.example.ssb.gto.gpe.Entity.Gpe;
import com.example.ssb.gto.gpe.Repository.GpeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GpeService {

    private final GpeRepository gpeRepository;
    private final Cloudinary cloudinary;


    public List<Gpe> getSampleGpe() {
        return gpeRepository.findBySampleTrue();
    }

    // ✅ Get All
    public List<Gpe> getAllGpe() {
        return gpeRepository.findAll();
    }

    // ✅ Get By ID
    public Gpe getById(String id) {
        return gpeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GPE not found with id: " + id));
    }

    // ✅ Create
    public Gpe create(Gpe gpe, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = uploadToCloudinary(file);
                gpe.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }
        return gpeRepository.save(gpe);
    }



    // ✅ Patch (Partial Update)
    public Gpe patch(String id, Gpe gpe, MultipartFile file) {
        Gpe existing = getById(id);

        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = uploadToCloudinary(file);
                existing.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }

        existing.setSample(gpe.isSample());
        if (gpe.getImageUrl() != null && (file == null || file.isEmpty())) existing.setImageUrl(gpe.getImageUrl()); // Only if no new file
        if (gpe.getQuestion() != null) existing.setQuestion(gpe.getQuestion());
        if(gpe.getOverview()!=null) existing.setOverview(gpe.getOverview());

        if (gpe.getPlans() != null) existing.setPlans(gpe.getPlans());

        return gpeRepository.save(existing);
    }

    private String uploadToCloudinary(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "gto"));
        return uploadResult.get("url").toString();
    }

    // ✅ Delete
    public void delete(String id) {
        Gpe gpe = getById(id);
        if (gpe.getImageUrl() != null) {
            String imageUrl = gpe.getImageUrl();
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    String publicId = extractPublicId(imageUrl);
                    if (publicId != null) {
                        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                        log.info("Async delete GPE image: {} Result: {}", publicId, result);
                    }
                } catch (IOException e) {
                    log.error("Async delete failed for GPE image: {}", imageUrl, e);
                }
            });
        }
        gpeRepository.deleteById(id);
    }

    // ✅ Toggle isSample
    public Gpe toggleSample(String id) {
        Gpe gpe = getById(id);
        gpe.setSample(!gpe.isSample());
        return gpeRepository.save(gpe);
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

