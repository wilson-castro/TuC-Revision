package io.github.wilsoncastro.tucrevision.core.service;

import io.github.wilsoncastro.tucrevision.core.model.User;
import io.github.wilsoncastro.tucrevision.core.pojo.LoggedUserDTO;
import io.github.wilsoncastro.tucrevision.core.repository.UserRepository;
import io.github.wilsoncastro.tucrevision.core.security.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationMs;

    public String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public LoggedUserDTO login(String email, String password) {

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Email e/ou senha incorretos"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = generateTokenFromEmail(userDetails.getEmail());

        log.info("User {} successfully logged in with email {}", userDetails.getId(), userDetails.getEmail());

        return new LoggedUserDTO(user.getId(), user.getName(), user.getEmail(), jwtToken);
    }

}
