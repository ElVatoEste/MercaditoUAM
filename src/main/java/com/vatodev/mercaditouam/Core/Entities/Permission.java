package com.vatodev.mercaditouam.Core.Entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tbl_Permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Column(nullable = false, unique = true)
    private String permissionName;  // Asegúrate de que coincide con setPermissionName

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
