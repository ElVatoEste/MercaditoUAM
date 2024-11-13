package com.vatodev.mercaditouam.Core.Repositories;

import com.vatodev.mercaditouam.Core.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
