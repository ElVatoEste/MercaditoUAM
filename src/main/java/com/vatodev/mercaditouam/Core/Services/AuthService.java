package com.vatodev.mercaditouam.Core.Services;

import com.vatodev.mercaditouam.Core.Dtos.AuthRequest;
import com.vatodev.mercaditouam.Core.Dtos.AuthResponse;
import com.vatodev.mercaditouam.Core.Exceptions.AuthenticationFailedException;
import com.vatodev.mercaditouam.Core.Exceptions.InvalidRefreshTokenException;
import com.vatodev.mercaditouam.Core.Exceptions.UserAlreadyExistsException;
import com.vatodev.mercaditouam.Core.Security.JWT.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest authRequest) {
        try {
            // Autenticar al usuario
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception e) {
            throw new AuthenticationFailedException();
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // Generar tokens
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

        return new AuthResponse(jwt, refreshToken);
    }

    public AuthResponse register(AuthRequest authRequest) {
        String hashedPassword = passwordEncoder.encode(authRequest.getPassword());
        Timestamp dateCreated = new Timestamp(new Date().getTime());

        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL Registrar_Usuario(?, ?, ?, ?, ?, ?, ?, ?)}")) {

            // Parámetros de usuario
            statement.setString(1, authRequest.getUsername());
            statement.setString(2, authRequest.getEmail());
            statement.setString(3, hashedPassword);
            statement.setTimestamp(4, dateCreated);

            // Parámetros de perfil
            statement.setString(5, authRequest.getCIF());
            statement.setString(6, authRequest.getPhoneNumber());
            statement.setString(7, authRequest.getDescription());
            statement.setBytes(8, authRequest.getProfilePicture()); // Asumiendo que el profilePicture es un byte[]

            statement.execute();

        } catch (SQLException ex) {
            if (ex.getErrorCode() == 2627 || ex.getMessage().contains("Username or Email already exists")) {
                throw new UserAlreadyExistsException();
            } else {
                throw new RuntimeException("Error al registrar el usuario: " + ex.getMessage());
            }
        }
        return login(authRequest);
    }


    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);

        if (username == null || !jwtUtil.validateRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newJwt = jwtUtil.generateToken(userDetails.getUsername());

        return new AuthResponse(newJwt, refreshToken);
    }

    public ResponseEntity<?> logout() {
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("Logout successful. Cookies cleared.");
    }
}
