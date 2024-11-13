package com.vatodev.mercaditouam.Core.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;

@Service
public class PermissionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Verifica si el usuario tiene el permiso especificado.
     * @param userId El ID del usuario.
     * @param permissionName El nombre del permiso.
     * @return true si el usuario tiene el permiso, false en caso contrario.
     */

    public boolean hasPermission(Long userId, String permissionName) {
        String sql = "{call Check_UserPermission(?, ?)}";

        Integer result = jdbcTemplate.execute(
                sql,
                (CallableStatement cs) -> {
                    cs.setLong(1, userId);
                    cs.setString(2, permissionName);
                    cs.execute();
                    return cs.getResultSet().next() ? cs.getResultSet().getInt("HasPermission") : 0;
                }
        );
        return result != null && result == 1;
    }
}
