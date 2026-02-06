package com.example.ssb.pschycological.srt;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/srt/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SrtTestAdminController {

    private final SrtTestService service;

    @PostMapping("/tests")
    public ResponseEntity<SrtTest> createTest(@RequestBody SrtTest test) {
        return ResponseEntity.ok(service.createTest(test));
    }

    @PatchMapping("/tests/{id}")
    public ResponseEntity<SrtTest> updateFullTest(
            @PathVariable String id,
            @RequestBody SrtTest updatedTest) {

        return ResponseEntity.ok(service.updateFullTest(id, updatedTest));
    }


    @DeleteMapping("/tests/{id}")
    public ResponseEntity<String> deleteTest(@PathVariable String id) {
        service.deleteTest(id);
        return ResponseEntity.ok("SRT Test deleted successfully");
    }
}

