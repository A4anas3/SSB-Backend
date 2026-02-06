package com.example.ssb.interview.controller;



import com.example.ssb.interview.RapidFireQuestion;
import com.example.ssb.interview.RapidFireQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/interview/rapid-fire")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminRapidFireQuestionController {

    private final RapidFireQuestionService service;

    // ✅ CREATE (return String)
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody RapidFireQuestion question) {
        service.create(question);
        return new ResponseEntity<>("Rapid Fire Question created successfully", HttpStatus.CREATED);
    }

    // ✅ PATCH (return String)
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> patch(@PathVariable String id, @RequestBody RapidFireQuestion question) {
        service.patch(id, question);
        return ResponseEntity.ok("Rapid Fire Question updated successfully");
    }

    // ✅ DELETE (already String ✅)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Rapid Fire Question deleted successfully");
    }
}


