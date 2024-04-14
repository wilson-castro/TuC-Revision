package com.projeto.pi.projeto_pi.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.projeto.pi.projeto_pi.utils.ReponseBuilder;

import jakarta.servlet.ServletException;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ErrorController {
    ReponseBuilder rb = new ReponseBuilder();

    @ExceptionHandler(NullPointerException.class) // exception handled
    public ResponseEntity<?> handleNullPointerExceptions(
            Exception e) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);

        String[] stackTrace = stringWriter.toString().split("\n");
        // Somente as 3 primeiras linhas
        stackTrace = Arrays.copyOfRange(stackTrace, 0, 3);

        HttpStatus status = HttpStatus.NOT_FOUND; // 404

        return rb.error("Erro NPE:" + String.join(",", stackTrace), status);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleExceptions(
            TokenExpiredException e) {

        HttpStatus status = HttpStatus.FORBIDDEN; // 403

        return rb.error("Token expirado", status);

    }

    // fallback method
    @ExceptionHandler(Exception.class) // exception handled
    public ResponseEntity<?> handleExceptions(
            Exception e) {
        // ... potential custom logic

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        // converting the stack trace to String
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        // ... potential custom logic

        return rb.error("Ocorreu um erro interno:" + e.getMessage(), status);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // exception handled
    public ResponseEntity<?> handleExceptions(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AuthenticationException.class) // exception handled
    public ResponseEntity<?> handleExceptions(
            AuthenticationException e) {

        return rb.error("Erro ao autenticar:" + e.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ServletException.class) // exception handled
    public ResponseEntity<?> handleExceptions(
            ServletException e) {

        return rb.error(e.getMessage(), HttpStatus.BAD_REQUEST);

    }
}
