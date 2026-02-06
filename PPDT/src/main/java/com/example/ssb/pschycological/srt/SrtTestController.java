package com.example.ssb.pschycological.srt;




import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/srt/user")
@RequiredArgsConstructor
public class SrtTestController {

    private final SrtTestService service;

    @GetMapping("/tests/names")
    public ResponseEntity<List<TestNameDto>> getTestNames() {
        return ResponseEntity.ok(service.getAllTestNames());
    }

    @GetMapping("/tests/{id}")
    public ResponseEntity<SrtTest> getTest(@PathVariable String id) {
        return ResponseEntity.ok(service.getTestById(id));
    }
}
