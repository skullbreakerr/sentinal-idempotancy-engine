package com.example.sentinal_idempotancy_engine.config;

import com.example.sentinal_idempotancy_engine.config.ShieldGateFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ShieldGateFilter shieldGateFilter;

    public SecurityConfig(ShieldGateFilter shieldGateFilter) {
        this.shieldGateFilter = shieldGateFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF (Standard for Stateless APIs)
            .csrf(csrf -> csrf.disable())

            // 2. Handle CORS - Crucial for "X-Request-ID" (Sentinel) to pass through
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("*"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                config.setAllowedHeaders(List.of("Content-Type", "X-Request-ID", "Authorization"));
                return config;
            }))

            // 3. Set Session to Stateless (No JSESSIONID)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 4. Endpoints Protection
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/payments/**").permitAll() // Public for the demo
                .requestMatchers("/actuator/**").hasRole("ADMIN")        // Protected monitoring
                .anyRequest().authenticated()
            )

            // 5. INJECT SHIELDGATE: Put your rate limiter BEFORE the Auth filter
            // This saves server resources by rejecting bots before checking passwords.
            .addFilterBefore(shieldGateFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
