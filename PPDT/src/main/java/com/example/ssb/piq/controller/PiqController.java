package com.example.ssb.piq.controller;

import com.example.ssb.piq.entity.PiqForm;
import com.example.ssb.piq.repo.PiqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PiqController {

    private final PiqRepository piqRepository;

    @GetMapping("/api/piq")
    public ResponseEntity<PiqForm> getPiq(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return piqRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new PiqForm())); // Return empty form if not found
    }

    @PostMapping("/api/piq")
    public ResponseEntity<PiqForm> savePiq(@AuthenticationPrincipal Jwt jwt, @RequestBody PiqForm piqForm) {
        String userId = jwt.getSubject();
        piqForm.setUserId(userId);

        // Extract user name and email from JWT
        piqForm.setUserEmail(jwt.getClaimAsString("email"));
        Map<String, Object> userMeta = jwt.getClaimAsMap("user_metadata");
        if (userMeta != null && userMeta.get("full_name") != null) {
            piqForm.setUserName(userMeta.get("full_name").toString());
        }

        // Check if exists to update or save new
        Optional<PiqForm> existing = piqRepository.findByUserId(userId);
        if (existing.isPresent()) {
            piqForm.setId(existing.get().getId());
        }

        return ResponseEntity.ok(piqRepository.save(piqForm));
    }

    /**
     * Admin-only: Get all PIQ forms
     */
    @GetMapping("/admin/piq")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PiqForm> getAllPiq() {
        return piqRepository.findAll();
    }

    /**
     * Admin-only: Delete a PIQ form by id
     */
    @DeleteMapping("/admin/piq/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePiq(@PathVariable String id) {
        piqRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
