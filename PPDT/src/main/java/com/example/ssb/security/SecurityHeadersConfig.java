package com.example.ssb.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Security Headers Filter
 * Adds CSP and other security headers to protect against XSS, clickjacking, etc.
 */
@Component
@Order(1)
public class SecurityHeadersConfig implements Filter {

    @org.springframework.beans.factory.annotation.Value("${app.frontend-url}")
    private String frontendUrl;

    @org.springframework.beans.factory.annotation.Value("${app.auth-url}")
    private String authUrl;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Build CSP dynamically
        String cspPolicy = String.join("; ",
                "default-src 'self'",
                "script-src 'self'",
                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com",
                "font-src 'self' https://fonts.gstatic.com",
                "connect-src 'self' " + authUrl + " " + frontendUrl,
                "img-src 'self' data: blob: https://res.cloudinary.com https://*.cloudinary.com https:",
                "frame-ancestors 'none'",
                "base-uri 'self'",
                "form-action 'self'"
        );

        // 🔐 Content Security Policy - Prevents XSS by restricting script sources
        httpResponse.setHeader("Content-Security-Policy", cspPolicy);

        // 🔐 Prevent MIME-type sniffing
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // 🔐 Prevent clickjacking
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // 🔐 Legacy XSS filter (for older browsers)
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // 🔐 Control referrer information
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // 🔐 Prevent caching of sensitive data
        httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        httpResponse.setHeader("Pragma", "no-cache");

        chain.doFilter(request, response);
    }
}
