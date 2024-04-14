package io.github.wilsoncastro.tucrevision.core.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private Map<String, String> buildError(Exception ex, HttpServletRequest request) {
        Map<String, String> err = new HashMap<>();
        err.put("timestamp", Instant.now().toString());
        err.put("status", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        err.put("error", ex.getMessage());
        err.put("path", request.getRequestURI());
        return err;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> globalExceptionHandler(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildError(ex, request));
    }

}
