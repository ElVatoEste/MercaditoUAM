package com.vatodev.mercaditouam.Core.Security;

import com.vatodev.mercaditouam.Core.Services.PermissionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final PermissionService permissionService;

    public CustomAuthorizationFilter(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String permissionName = extractPermissionName(request);
        Long userId = getUserId();

        if (userId != null && permissionName != null && !permissionService.hasPermission(userId, permissionName)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("El usuario no tiene permiso para acceder a este recurso.");
            return;
        }
        chain.doFilter(request, response);
    }

    private Long getUserId() {
        // Obtén el contexto de seguridad y verifica si contiene autenticación
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // Obtiene el ID del usuario si el principal es una instancia de CustomUserDetails
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUserId();
        }
        return null;
    }

    private String extractPermissionName(HttpServletRequest request) {
        // Extrae la URI sin el prefijo "api/" y luego reemplaza los "/" por ":"
        String path = request.getRequestURI().replaceFirst("^/api/", "").replace("/", ":");
        // Crea el permiso en el formato "{metodo}:{path}"
        return path;
    }

}
