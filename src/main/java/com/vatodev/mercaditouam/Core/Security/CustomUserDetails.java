package com.vatodev.mercaditouam.Core.Security;

import com.vatodev.mercaditouam.Core.Entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .filter(role -> role.getIsActive()) // Filtra roles activos
                .flatMap(role -> role.getPermissions().stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName())))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Puedes personalizar esta lógica si tu entidad User tiene campos adicionales para gestionar expiraciones
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Igualmente, puedes cambiar la lógica si tienes un campo para esto
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Cambiar según tu lógica de negocio
    }

    @Override
    public boolean isEnabled() {
        return user.getActive(); // Controla si el usuario está activo
    }
}
