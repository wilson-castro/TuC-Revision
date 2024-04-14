package com.projeto.pi.projeto_pi.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.projeto.pi.projeto_pi.modals.users.User;

@Service
public class TokenService {

    @Value("${jwt.token.exp:600}")
    private Long tokenExpiration;

    @Value("${jwt.token.refreshExp:600}")
    private Long refreshTokenExpiration;

    // Não é o mais seguro, mas serve pro proposito :/
    @Value("${jwt.token.secret:43cu95n8r34v8975yb834589tvm456049nt4y5o0}")
    private String secret;

    @Value("${jwt.token.refreshSecret:aslk3j4u32poaur9v324b}")
    private String refreshSecret;

    public String generateToken(User user) {
        return this.generateTokenInternal(user, this.getExpirationDateFromToken(), secret);
    }

    public String verify(String token) {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getSubject();
    }

    public Instant getExpirationDateFromToken() {

        return new Date(System.currentTimeMillis())
                .toInstant().plusSeconds(tokenExpiration);

    }

    /// Refresh token
    public String generateRefreshToken(User user) {
        return this.generateTokenInternal(user, this.getExpirationDateFromRefreshToken(), refreshSecret);
    }

    public String verifyRefresh(String token) {
        return JWT.require(Algorithm.HMAC256(refreshSecret)).build().verify(token).getSubject();
    }

    public Instant getExpirationDateFromRefreshToken() {

        return new Date(System.currentTimeMillis())
                .toInstant().plusSeconds(refreshTokenExpiration);

    }

    private String generateTokenInternal(User user, Instant exp, String secret) {
        return JWT.create()
                .withClaim("carros", true)
                .withClaim("users", user.getRole().toUpperCase().contains("ADMIN"))
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withExpiresAt(
                        exp)
                .sign(Algorithm.HMAC256(secret));
    }
}
