package com.example.ssb.feedback.controller;

import com.example.ssb.feedback.entity.Feedback;
import com.example.ssb.feedback.repo.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
@RestController
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;



    @PostMapping("/feedback")
    public Feedback submitFeedback(@RequestBody Map<String, Object> body,
                                   @AuthenticationPrincipal Jwt jwt) {
        Feedback fb = new Feedback();
        fb.setUserId(jwt.getSubject());
        fb.setUserEmail(jwt.getClaimAsString("email"));

        // Supabase stores name in user_metadata.full_name
        Map<String, Object> userMeta = jwt.getClaimAsMap("user_metadata");
        if (userMeta != null && userMeta.get("full_name") != null) {
            fb.setUserName(userMeta.get("full_name").toString());
        } else {
            fb.setUserName(jwt.getClaimAsString("email")); // fallback
        }

        if (body.get("subject") != null) {
            fb.setSubject(body.get("subject").toString());
        }
        if (body.get("improvement") != null) {
            fb.setImprovement(body.get("improvement").toString());
        }
        if (body.get("rating") != null) {
            try {
                fb.setRating(Integer.parseInt(body.get("rating").toString()));
            } catch (NumberFormatException e) {
                // Ignore or handle invalid number format
            }
        }

        return feedbackRepository.save(fb);
    }

    /**
     * GET /admin/feedback — admin-only, lists all feedback newest first.
     */
    @GetMapping("/admin/feedback")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Feedback> getAllFeedback() {
        List<Feedback> list = feedbackRepository.findAll();
        list.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return list;
    }
}
