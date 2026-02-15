package com.example.ssb.gto.lecturette.controller;

import com.example.ssb.gto.lecturette.Entity.Lecturette;
import com.example.ssb.gto.lecturette.service.LecturetteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController

@RequestMapping("admin/gto/lecturette")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class Admin {
    private final LecturetteService lecturetteService;
    private final ObjectMapper objectMapper;

    @DeleteMapping("delete/{id}")

    public ResponseEntity<String> deleteLecturette(@PathVariable String id) {
        lecturetteService.deleteLecturette(id);
        return ResponseEntity.ok("Lecturette deleted successfully");
    }
    
    @PatchMapping(value = "patch/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Lecturette> patchLecturette(@PathVariable String id,
                                                      @RequestPart("lecturette") String lecturetteJson,
                                                      @RequestPart(value = "image", required = false) MultipartFile image) throws JsonProcessingException {
        Lecturette lecturette = objectMapper.readValue(lecturetteJson, Lecturette.class);
        Lecturette updated = lecturetteService.patchLecturette(id, lecturette, image);
        return ResponseEntity.ok(updated);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> createLecturette(
            @RequestPart("lecturette") String lecturetteJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws JsonProcessingException {
        Lecturette lecturette = objectMapper.readValue(lecturetteJson, Lecturette.class);
        lecturetteService.createLecturette(lecturette, image);
        return new ResponseEntity<>("Lecturette created successfully", HttpStatus.CREATED);
    }


    // ✅ UPDATE
    @PutMapping("update/full/{id}")
    public ResponseEntity<Lecturette> updateLecturette(@PathVariable String id,
                                                       @RequestBody Lecturette lecturette) {
        Lecturette updated = lecturetteService.updateLecturette(id, lecturette);
        return ResponseEntity.ok(updated);
    }

}
