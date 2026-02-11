package com.example.ssb.pschycological.wat.controller;



import com.example.ssb.pschycological.wat.dto.TestNameDto;
import com.example.ssb.pschycological.wat.entity.WordTest;
import com.example.ssb.pschycological.wat.service.WordTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wat/user")
@RequiredArgsConstructor
public class WordTestController {

    private final WordTestService service;
    @GetMapping("/tests/names")
    public ResponseEntity<List<TestNameDto>> getTestNames() {
        return ResponseEntity.ok(service.getAllTestNames());
    }


    @GetMapping("/tests/{id}")
    public ResponseEntity<WordTest> getTest(@PathVariable String id) {
        return ResponseEntity.ok(service.getTestById(id));
    }


}
