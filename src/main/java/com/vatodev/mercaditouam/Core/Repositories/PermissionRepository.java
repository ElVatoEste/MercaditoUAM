package com.vatodev.mercaditouam.Core.Repositories;

import com.vatodev.mercaditouam.Core.Entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String permissionName);
}
