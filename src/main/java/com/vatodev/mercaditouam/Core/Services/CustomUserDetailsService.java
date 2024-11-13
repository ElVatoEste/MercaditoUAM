package com.vatodev.mercaditouam.Core.Services;

import com.vatodev.mercaditouam.Core.Entities.User;
import com.vatodev.mercaditouam.Core.Repositories.UserRepository;
import com.vatodev.mercaditouam.Core.Security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new CustomUserDetails(user);
    }
}
