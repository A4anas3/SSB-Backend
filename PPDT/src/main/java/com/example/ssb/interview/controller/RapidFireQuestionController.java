package com.example.ssb.interview.controller;



import com.example.ssb.interview.RapidFireQuestion;
import com.example.ssb.interview.RapidFireQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interview/rapid-fire")
@RequiredArgsConstructor
public class RapidFireQuestionController {

    private final RapidFireQuestionService service;

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<RapidFireQuestion>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RapidFireQuestion> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
