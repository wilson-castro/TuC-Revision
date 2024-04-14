package com.projeto.pi.projeto_pi.Spring;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.pi.projeto_pi.utils.ReponseBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AccessEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        ReponseBuilder er = new ReponseBuilder();
        ResponseEntity<Map<String, Object>> error = er.error("Erro:" + authException.getMessage(),
                HttpStatus.FORBIDDEN);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(error.getBody());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(json);
    }

}
