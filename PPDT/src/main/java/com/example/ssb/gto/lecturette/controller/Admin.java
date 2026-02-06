package com.example.ssb.gto.lecturette.controller;

import com.example.ssb.gto.lecturette.Entity.Lecturette;
import com.example.ssb.gto.lecturette.service.LecturetteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("admin/gto/lecturette")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class Admin {
    private final LecturetteService lecturetteService;
    @DeleteMapping("delete/{id}")

    public ResponseEntity<String> deleteLecturette(@PathVariable String id) {
        lecturetteService.deleteLecturette(id);
        return ResponseEntity.ok("Lecturette deleted successfully");
    }
    @PatchMapping("patch/{id}")
    public ResponseEntity<Lecturette> patchLecturette(@PathVariable String id,
                                                      @RequestBody Lecturette lecturette) {
        Lecturette updated = lecturetteService.patchLecturette(id, lecturette);
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<String> createLecturette(@RequestBody Lecturette lecturette) {
        lecturetteService.createLecturette(lecturette);
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
