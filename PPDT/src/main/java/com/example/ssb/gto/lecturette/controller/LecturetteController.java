package com.example.ssb.gto.lecturette.controller;

import com.example.ssb.gto.lecturette.Entity.Lecturette;
import com.example.ssb.gto.lecturette.dto.LecturetteAnalysisRequest;
import com.example.ssb.gto.lecturette.dto.LecturetteAnalysisResponse;

import com.example.ssb.gto.lecturette.dto.basic;
import com.example.ssb.gto.lecturette.service.LecturetteService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gto/lecturette")
@RequiredArgsConstructor
public class LecturetteController {

    private final LecturetteService lecturetteService;

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<Lecturette>> getAllLecturettes() {
        List<Lecturette> lecturettes = lecturetteService.getAllLecturettes();
        return ResponseEntity.ok(lecturettes);
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Lecturette> getLecturetteById(@PathVariable String id) {
        Lecturette lecturette = lecturetteService.getLecturetteById(id);
        return ResponseEntity.ok(lecturette);
    }



    // ✅ DELETE

    @GetMapping("/search")
    public ResponseEntity<List<Lecturette>> searchByTitle(@RequestParam String keyword) {
        List<Lecturette> result = lecturetteService.searchByTitle(keyword);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/search/category")
    public ResponseEntity<List<Lecturette>> searchByCategory(@RequestParam String category) {
        List<Lecturette> result = lecturetteService.searchByCategory(category);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(lecturetteService.getDistinctCategories());
    }

    @GetMapping("/basic")
    public ResponseEntity<List<basic>> getBasicLecturettes() {
        return ResponseEntity.ok(lecturetteService.getBasicLecturettes());
    }
    // ===============================
// 🎯 RANDOM TOPIC FOR TEST
// ===============================
    @GetMapping("/test/random")
    public ResponseEntity<Lecturette> getRandomLecturette() {
        return ResponseEntity.ok(lecturetteService.getRandomLecturette());
    }

 
    // ===============================
// 🤖 SEND TO AI SERVICE
// ===============================
    @PostMapping("/test/analyse")
    public ResponseEntity<LecturetteAnalysisResponse> analyseLecturette(
            @RequestBody LecturetteAnalysisRequest request) {

        LecturetteAnalysisResponse response =
                lecturetteService.analyse(request);

        return ResponseEntity.ok(response);
    }





}
