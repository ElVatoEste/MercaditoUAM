package com.vatodev.mercaditouam.Core.Security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpirationInMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Generar un token JWT
    public String generateToken(String username) {
        return createToken(username, jwtExpirationInMs);
    }

    // Generar un refresh token
    public String generateRefreshToken(String username) {
        return createToken(username, refreshExpirationInMs);
    }

    // Método privado para crear tokens con expiración específica
    private String createToken(String username, long expirationInMs) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Obtener el usuario a partir del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraer cualquier claim (reclamación) del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validar token JWT
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Validar el refresh token (sin validar el username, solo la expiración)
    public boolean validateRefreshToken(String refreshToken) {
        return !isTokenExpired(refreshToken);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
