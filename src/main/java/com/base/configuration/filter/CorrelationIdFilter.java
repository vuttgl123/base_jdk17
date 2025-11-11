package com.base.configuration.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "X-Correlation-Id";
    public static final String MDC_KEY = "correlationId";

    // Cache các path được exclude để tránh string comparison lặp lại
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/actuator/health",
            "/actuator/info",
            "/actuator/metrics",
            "/favicon.ico"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId = extractCorrelationId(request);

        try {
            MDC.put(MDC_KEY, correlationId);
            response.setHeader(HEADER_NAME, correlationId);

            filterChain.doFilter(request, response);
        } finally {
            // Đảm bảo MDC luôn được clear
            MDC.remove(MDC_KEY);
        }
    }

    private String extractCorrelationId(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_NAME);

        if (StringUtils.hasText(headerValue)) {
            // Validate UUID format nếu cần (optional)
            return headerValue;
        }

        return generateCorrelationId();
    }

    private String generateCorrelationId() {
        // Sử dụng randomUUID (thread-safe và đủ nhanh)
        return UUID.randomUUID().toString();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // Sử dụng Set lookup (O(1)) thay vì multiple startsWith
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }
}