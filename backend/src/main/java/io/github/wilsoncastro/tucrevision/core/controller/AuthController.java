package io.github.wilsoncastro.tucrevision.core.controller;

import io.github.wilsoncastro.tucrevision.core.pojo.LoggedUserDTO;
import io.github.wilsoncastro.tucrevision.core.pojo.LoginDTO;
import io.github.wilsoncastro.tucrevision.core.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoggedUserDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok().body(authService.login(loginDTO.email().toLowerCase(), loginDTO.password()));
    }

}
