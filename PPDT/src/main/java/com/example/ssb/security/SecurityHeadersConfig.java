package com.example.ssb.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Security Headers Filter
 * Adds CSP and other security headers
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

        // =========================
        // CONTENT SECURITY POLICY
        // =========================
        String cspPolicy = String.join("; ",

                // default
                "default-src 'self'",

                // scripts (React/Vite support)
                "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdn.jsdelivr.net",

                // styles
                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com",

                // fonts
                "font-src 'self' https://fonts.gstatic.com data:",

                // 🔥 MOST IMPORTANT (API CALLS)
                "connect-src 'self' " + frontendUrl + " " + authUrl +
                        " https://*.supabase.co" +
                        " wss://*.supabase.co" +
                        " https://api.cloudinary.com" +
                        " https://*.railway.app" +
                        " https://*.cloudflare.com" +
                        " https://*.cloudflareworkers.com",

                // images
                "img-src 'self' data: blob: https://res.cloudinary.com https://*.cloudinary.com",

                // security
                "frame-ancestors 'none'",
                "base-uri 'self'",
                "form-action 'self'"
        );

        httpResponse.setHeader("Content-Security-Policy", cspPolicy);

        // =========================
        // OTHER SECURITY HEADERS
        // =========================
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-Frame-Options", "DENY");
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        httpResponse.setHeader("Pragma", "no-cache");

        chain.doFilter(request, response);
    }
}