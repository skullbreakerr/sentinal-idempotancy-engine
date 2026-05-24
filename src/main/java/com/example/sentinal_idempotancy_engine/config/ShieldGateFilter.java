package com.example.sentinal_idempotancy_engine.config;

import com.example.sentinal_idempotancy_engine.service.ShieldGateService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ShieldGateFilter extends OncePerRequestFilter {

    private final ShieldGateService shieldGateService;

    public ShieldGateFilter(ShieldGateService shieldGateService) {
        this.shieldGateService = shieldGateService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Identify the user by their IP Address (or a JWT User ID if available)
        String clientIp = request.getRemoteAddr();

        if (shieldGateService.allowRequest(clientIp)) {
            filterChain.doFilter(request, response);
        } else {
            handleRateLimitError(response);
        }
    }

    private void handleRateLimitError(HttpServletResponse response) throws IOException {
        response.setStatus(429); // Too Many Requests
        response.setContentType("application/json");
        response.getWriter().write(
                "{ \"error\": \"ShieldGate: Security Policy Violation\", \"message\": \"Rate limit exceeded. Please wait before retrying your transaction.\" }");
    }
}