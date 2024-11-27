package com.vatodev.mercaditouam.Core.Controllers;

import com.vatodev.mercaditouam.Core.Dtos.AuthResponse;
import com.vatodev.mercaditouam.Core.Dtos.LoginRequest;
import com.vatodev.mercaditouam.Core.Dtos.RegisterRequest;
import com.vatodev.mercaditouam.Core.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest authRequest) {
        try {
            AuthResponse authResponse = authService.login(authRequest);

            // Crear cookies para JWT y Refresh Token
            ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", authResponse.getAccessToken())
                    .httpOnly(false)
                    .secure(true) // Activar solo para HTTPS en producción
                    .path("/")
                    .maxAge(Duration.ofMinutes(60))
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", authResponse.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(authResponse);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error de autenticación: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest authRequest) {
        try {
            AuthResponse authResponse = authService.register(authRequest);

            // Crear cookies para JWT y Refresh Token después del registro exitoso
            ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", authResponse.getAccessToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofMinutes(15))
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", authResponse.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(authResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        try {
            AuthResponse response = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Token de refresco inválido o expirado.");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return authService.logout();
    }
}
