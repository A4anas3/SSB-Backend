package com.example.ssb;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class debug {
    @GetMapping("/debug/auth")
    public Map<String, Object> debug(Authentication auth) {

        return Map.of(
                "username", auth.getName(),
                "authorities", auth.getAuthorities()
        );
    }
}
