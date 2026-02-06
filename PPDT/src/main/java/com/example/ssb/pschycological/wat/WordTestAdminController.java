package com.example.ssb.pschycological.wat;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wat/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class WordTestAdminController {

    private final WordTestService service;

    // ✅ Create test
    @PostMapping("/tests")
    public ResponseEntity<WordTest> createTest(@RequestBody WordTest test) {
        return ResponseEntity.ok(service.createTest(test));
    }

    // ✅ Patch word or sentence
    @PatchMapping("/tests/{id}")
    public ResponseEntity<WordTest> updateFullTest(
            @PathVariable String id,
            @RequestBody WordTest updatedTest) {

        return ResponseEntity.ok(service.updateFullTest(id, updatedTest));
    }


    // ✅ Delete test
    @DeleteMapping("/tests/{id}")
    public ResponseEntity<String> deleteTest(@PathVariable String id) {
        service.deleteTest(id);
        return ResponseEntity.ok("Test deleted successfully");
    }
}
