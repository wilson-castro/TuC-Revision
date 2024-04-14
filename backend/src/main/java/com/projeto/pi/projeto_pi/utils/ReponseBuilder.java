package com.projeto.pi.projeto_pi.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

public class ReponseBuilder {
    public ResponseEntity<Map<String, Object>> error(String message, HttpStatus code) {
        java.util.Map<String, Object> error = new HashMap<>();

        UriComponents path = ServletUriComponentsBuilder.fromCurrentRequest().build();

        String params = "/" + String.join("/", path.getPathSegments());
        String query = path.getQuery();

        error.put("status", code.getReasonPhrase());
        error.put("message", message);
        error.put("code", code.value());
        error.put("timestamp", System.currentTimeMillis());
        error.put("path", params + (query != null ? "?" + query : ""));

        return new ResponseEntity<>(error, code);
    }

    public ResponseEntity<Map<String, Object>> error(String message) {
        java.util.Map<String, Object> error = new HashMap<>();
        HttpStatus code = HttpStatus.BAD_REQUEST;
        UriComponents path = ServletUriComponentsBuilder.fromCurrentRequest().build();

        String params = "/" + String.join("/", path.getPathSegments());
        String query = path.getQuery();

        error.put("status", code.getReasonPhrase());
        error.put("message", message);
        error.put("code", code.value());
        error.put("timestamp", System.currentTimeMillis());
        error.put("path", params + (query != null ? "?" + query : ""));

        return new ResponseEntity<>(error, code);
    }

    public ResponseEntity<Map<String, Object>> success(Object data) {
        java.util.Map<String, Object> success = new HashMap<>();
        HttpStatus code = HttpStatus.OK;
        UriComponents path = ServletUriComponentsBuilder.fromCurrentRequest().build();
        String params = "/" + String.join("/", path.getPathSegments());
        String query = path.getQuery();

        success.put("status", code.getReasonPhrase());
        success.put("data", data);
        success.put("code", code.value());
        success.put("timestamp", System.currentTimeMillis());
        success.put("path", params + (query != null ? "?" + query : ""));
        return new ResponseEntity<>(success, code);
    }
}
