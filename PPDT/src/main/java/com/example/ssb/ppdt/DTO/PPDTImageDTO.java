package com.example.ssb.ppdt.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PPDTImageDTO {

    private MultipartFile image;
    private String imageContext;
    private String guide;

    private Boolean isSample;
    private String action;
    private String sampleStory;
}