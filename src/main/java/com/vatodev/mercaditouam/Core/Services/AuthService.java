package com.vatodev.mercaditouam.Core.Services;

import com.vatodev.mercaditouam.Core.Dtos.RegisterRequest;
import com.vatodev.mercaditouam.Core.Dtos.AuthResponse;
import com.vatodev.mercaditouam.Core.Dtos.LoginRequest;
import com.vatodev.mercaditouam.Core.Security.CustomUserDetails;
import com.vatodev.mercaditouam.Core.Exceptions.AuthenticationFailedException;
import com.vatodev.mercaditouam.Core.Exceptions.InvalidRefreshTokenException;
import com.vatodev.mercaditouam.Core.Exceptions.UserAlreadyExistsException;
import com.vatodev.mercaditouam.Core.Security.JWT.JwtUtil;
import com.vatodev.mercaditouam.Utils.ImageUtils;
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
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // Autenticar al usuario
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            throw new AuthenticationFailedException();
        }

        // Cargar detalles del usuario y roles
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginRequest.getUsername());
        Long userId = customUserDetails.getUserId();

        // Generar tokens con claims personalizados (userId y roles)
        final String jwt = jwtUtil.generateToken(userId, customUserDetails.getUsername());
        final String refreshToken = jwtUtil.generateRefreshToken(userId, customUserDetails.getUsername());

        return new AuthResponse(jwt, refreshToken);
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        Timestamp dateCreated = new Timestamp(new Date().getTime());

        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL Registrar_Usuario(?, ?, ?, ?, ?, ?, ?, ?)}")) {

            statement.setString(1, registerRequest.getUsername());
            statement.setString(2, registerRequest.getEmail());
            statement.setString(3, hashedPassword);
            statement.setTimestamp(4, dateCreated);
            statement.setString(5, registerRequest.getCif());
            statement.setString(6, registerRequest.getPhoneNumber());
            statement.setString(7, registerRequest.getDescription());

            // Manejo de la imagen de perfil
            if (registerRequest.getProfilePicture() != null && !registerRequest.getProfilePicture().isEmpty()) {
                byte[] compressedImage = ImageUtils.compressImage(registerRequest.getProfilePicture().getBytes());
                statement.setBytes(8, compressedImage);
            } else {
                statement.setNull(8, java.sql.Types.VARBINARY);
            }

            statement.execute();

        } catch (SQLException | IOException ex) {
            if (ex instanceof SQLException && ((SQLException) ex).getErrorCode() == 2627) {
                throw new UserAlreadyExistsException();
            }
            throw new RuntimeException("Error al registrar el usuario: " + ex.getMessage());
        }

        // Iniciar sesión automáticamente después de registrar al usuario
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(registerRequest.getUsername());
        loginRequest.setPassword(registerRequest.getPassword());
        return login(loginRequest);
    }

    public AuthResponse refreshToken(String refreshToken) {
        // Extrae el username del refresh token
        String username = jwtUtil.extractUsername(refreshToken);

        // Valida el token y si es inválido, lanza excepción
        if (username == null || !jwtUtil.validateRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        // Cargar los detalles completos del usuario, incluyendo roles
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        Long userId = customUserDetails.getUserId();

        // Genera un nuevo token JWT de acceso con userId y roles en los claims
        String newJwt = jwtUtil.generateToken(userId, username);

        // Devuelve el nuevo token de acceso junto con el mismo refresh token
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
