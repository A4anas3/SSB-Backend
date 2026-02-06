package com.example.ssb.ppdt.controller;

import com.example.ssb.ppdt.DTO.PPDTAnalysisResponseDTO;
import com.example.ssb.ppdt.DTO.PPDTSampleResponse;
import com.example.ssb.ppdt.DTO.PPDTSubmitRequest;
import com.example.ssb.ppdt.DTO.PPDTTestImageResponse;
import com.example.ssb.ppdt.Entity.PPDTImage;
import com.example.ssb.ppdt.Service.PPDTImageService;
import com.example.ssb.ppdt.Service.PPDTService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/ppdt")
@RequiredArgsConstructor
public class PPDTController {

    private final PPDTImageService imgservice;
    private final PPDTService service;

    @PostMapping("/submit")
    @Transactional
    public PPDTAnalysisResponseDTO submit( @RequestBody PPDTSubmitRequest req) {

        PPDTImage image = imgservice.getById(req.getImageId());

        return service.submit(
                req.getImageId(),
                image.getImageContext(),
                req.getStoryText(),
                req.getAction()
        );
    }

    @GetMapping("/test/images")
    public List<PPDTTestImageResponse> getTestImages() {
        return imgservice.getTestImages();
    }

    @GetMapping("/samples")
    public List<PPDTSampleResponse> getAllSamplePPDTs() {
        return imgservice.getAllSamplePPDTs();
    }

}
