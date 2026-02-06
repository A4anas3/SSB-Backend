package com.example.ssb.gto.gpe.controller;



import com.example.ssb.gto.gpe.Entity.Gpe;
import com.example.ssb.gto.gpe.dto.GpeSampleDto;
import com.example.ssb.gto.gpe.dto.GpeTestDto;
import com.example.ssb.gto.gpe.service.GpeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gto/gpe")
@RequiredArgsConstructor
public class GpeController {

    private final GpeService gpeService;
    @GetMapping("/{id}")
    public ResponseEntity<Gpe> getGpeById(@PathVariable String id) {
        return ResponseEntity.ok(gpeService.getById(id));
    }

    // ✅ 1. GET ALL GPE
    @GetMapping
    public ResponseEntity<List<Gpe>> getAll() {
        return ResponseEntity.ok(gpeService.getAllGpe());
    }

    @GetMapping("/sample")
    public ResponseEntity<List<GpeSampleDto>> getSampleGpe() {

        List<Gpe> list = gpeService.getSampleGpe();

        List<GpeSampleDto> result = list.stream()
                .map(g -> new GpeSampleDto(
                        g.getId(),

                        g.getImageUrl()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/test")
    public ResponseEntity<List<GpeTestDto>> getForTest() {

        List<Gpe> list = gpeService.getAllGpe();

        List<GpeTestDto> result = list.stream()
                .map(g -> new GpeTestDto(
                        g.getId(),
                        g.getImageUrl(),
                        g.getQuestion()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }


}

