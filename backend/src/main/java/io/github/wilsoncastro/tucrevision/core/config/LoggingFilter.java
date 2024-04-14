package io.github.wilsoncastro.tucrevision.core.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Log4j2
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        Long startTime = System.currentTimeMillis();

        String fishTag = UUID.randomUUID().toString();
        String ip = getUserRealIP(request);
        String path = request.getRequestURI();

        ThreadContext.put("id", fishTag);

        ThreadContext.put("ip", ip);
        ThreadContext.put("resource", path);
        ThreadContext.put("httpMethod", request.getMethod());

        log.info("Request for {} from {}", path, ip);

        filterChain.doFilter(request, response);

        Long endTime = System.currentTimeMillis();
        String latency = String.valueOf(endTime - startTime);

        ThreadContext.put("statusCode", String.valueOf(response.getStatus()));
        ThreadContext.put("latency", latency);

        log.info("Request for {} took {}ms", request.getRequestURI(), latency);
    }

    private String getUserRealIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
