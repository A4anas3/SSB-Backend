package com.example.ssb.ppdt.Service;

import com.example.ssb.ppdt.DTO.PPDTAdminImageResponse;
import com.example.ssb.ppdt.DTO.PPDTImageRequest;
import com.example.ssb.ppdt.DTO.PPDTSampleResponse;
import com.example.ssb.ppdt.DTO.PPDTTestImageResponse;
import com.example.ssb.ppdt.Entity.PPDTImage;
import com.example.ssb.ppdt.Repo.PPDTImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PPDTImageService {

    private final PPDTImageRepository imageRepo;

    /* =========================
       ADMIN: ADD IMAGE
       (always normal image)
       ========================= */
    public PPDTImage saveImage(PPDTImageRequest req) {

        PPDTImage image = new PPDTImage();
        image.setImageUrl(req.getImageUrl());
        image.setImageContext(req.getImageContext());
        image.setGuide(req.getGuide());

        image.setIsSample(false);
        image.setAction(null);
        image.setSampleStory(null);

        return imageRepo.save(image);
    }


    /* =========================
       ADMIN: UPDATE IMAGE
       ========================= */
    public PPDTImage updateImage(Long id, PPDTImageRequest req) {

        PPDTImage image = imageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        image.setImageUrl(req.getImageUrl());
        image.setImageContext(req.getImageContext());
        image.setGuide(req.getGuide());


        return imageRepo.save(image);
    }

    /* =========================
       ADMIN: DELETE IMAGE
       ========================= */
    public void deleteImage(Long id) {
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

}
