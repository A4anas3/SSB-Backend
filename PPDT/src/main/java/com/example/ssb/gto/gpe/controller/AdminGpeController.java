package com.example.ssb.gto.gpe.controller;

import com.example.ssb.gto.gpe.Entity.Gpe;
import com.example.ssb.gto.gpe.service.GpeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@AllArgsConstructor
@RequestMapping("admin/gto/gpe")
@PreAuthorize("hasRole('ADMIN')")
public class AdminGpeController {
    private final GpeService gpeService;
    // ✅ 3. CREATE GPE
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Gpe gpe) {
        gpeService.create(gpe);
        return new ResponseEntity<>("GPE created successfully", HttpStatus.CREATED);
    }



    // ✅ 5. PATCH
    @PatchMapping("/update/{id}")
    public ResponseEntity<Gpe> patch(@PathVariable String id, @RequestBody Gpe gpe) {
        return ResponseEntity.ok(gpeService.patch(id, gpe));
    }

    // ✅ 6. DELETE (ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        gpeService.delete(id);
        return ResponseEntity.ok("GPE deleted successfully");
    }

    // ✅ 7. TOGGLE isSample
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Gpe> toggleSample(@PathVariable String id) {
        return ResponseEntity.ok(gpeService.toggleSample(id));
    }
}
