package com.pongo.pongoedu.infrastructure.security;

import com.pongo.pongoedu.application.port.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider implements JwtProvider {
    @Value("${jwt.secret:mySecretKeyForSigningJWTTokensThatIsAtLeast256BitsLongForHS256Algorithm}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String gerarToken(Long usuarioId, String email) {
        SecretKey key = getSigningKey();
        return Jwts.builder()
            .subject(email)
            .claim("usuarioId", usuarioId)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(key)
            .compact();
    }

    @Override
    public Long extrairUsuarioId(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("usuarioId", Long.class);
    }

    @Override
    public String extrairEmail(String token) {
        Claims claims = getAllClaims(token);
        return claims.getSubject();
    }

    @Override
    public boolean validarToken(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getAllClaims(String token) {
        SecretKey key = getSigningKey();
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    @Override
    public long obterTempoExpiracao() {
        return jwtExpirationMs;
    }
}

