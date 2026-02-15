package com.example.ssb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ❌ CSRF not needed for JWT
                .csrf(csrf -> csrf.disable())

                // ✅ Enable CORS (uses CorsConfigurationSource bean)
                .cors(Customizer.withDefaults())

                // 🔐 Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // ✅ Allow preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ Public endpoints
                        .requestMatchers(
                                "/gto/gpe/sample",
                                "/gto/gpe/test",
                                "/gto/lecturette",
                                "/gto/lecturette/search",
                                "/gto/lecturette/api/oir/tests",
                                "/gto/lecturette/search/category",
                                "/gto/lecturette/basic",
                                "/interview/rapid-fire",
                                "/ppdt/samples",
                                "/srt/user/tests/names",
                                "/wat/user/tests/names"




                        ).permitAll()

                
                       .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 🔐 Everything else needs login
                        .anyRequest().authenticated()
                )

                // 🔐 OAuth2 Resource Server (JWT)
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

@Bean
public JwtAuthenticationConverter jwtAuthenticationConverter() {

    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

    jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {

        Map<String, Object> appMetadata = jwt.getClaimAsMap("app_metadata");

        if (appMetadata == null || !appMetadata.containsKey("role")) {
            return java.util.Collections.emptyList();
        }

        String role = (String) appMetadata.get("role");

        return java.util.List.of(
                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role)
        );
    });

    return jwtConverter;
}

   
    }

