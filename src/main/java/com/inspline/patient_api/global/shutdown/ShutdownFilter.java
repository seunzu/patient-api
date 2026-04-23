package com.inspline.patient_api.global.shutdown;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ShutdownFilter extends OncePerRequestFilter {

    private final ShutdownManager shutdownManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (shutdownManager.isShuttingDown()) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                {"message": "Server is shutting down", "data": null}
            """);
            return;
        }

        filterChain.doFilter(request, response);
    }
}