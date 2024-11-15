package com.vatodev.mercaditouam.Core.Security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
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

    // Generar un token JWT con claims adicionales
    public String generateToken(Long userId, String username) {
        return createToken(userId, username, jwtExpirationInMs);
    }

    // Generar un refresh token con claims adicionales
    public String generateRefreshToken(Long userId, String username) {
        return createToken(userId, username, refreshExpirationInMs); // Sin roles en el refresh token
    }

    // Método privado para crear tokens con claims personalizados y expiración específica
    private String createToken(Long userId, String username, long expirationInMs) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMs))
                .claim("userId", userId)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Obtener el nombre de usuario a partir del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Obtener el ID de usuario a partir del token
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    // Obtener roles a partir del token
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    // Extraer cualquier claim del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("El token JWT no puede ser nulo o vacío");
        }
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validar token JWT comparando el nombre de usuario y la expiración
    public boolean validateToken(String token, String username) {
        if (token == null || token.isEmpty()) return false;
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Validar el refresh token solo por expiración
    public boolean validateRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) return false;
        return !isTokenExpired(refreshToken);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
