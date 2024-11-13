package com.vatodev.mercaditouam.Core.Services;

import com.vatodev.mercaditouam.Core.Entities.Permission;
import com.vatodev.mercaditouam.Core.Entities.Role;
import com.vatodev.mercaditouam.Core.Repositories.PermissionRepository;
import com.vatodev.mercaditouam.Core.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;



@Service
public class InitializationService implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private org.springframework.context.ApplicationContext applicationContext;

    @Override
    @Transactional
    public void run(String... args) {
        // Escanear automáticamente todos los permisos desde los controladores
        Set<Permission> permissions = scanPermissionsFromControllers();

        // Crear roles y asignar permisos al rol de Admin
        createRoleWithPermissions("Admin", permissions);
        createRoleWithPermissions("User", new HashSet<>());  // Rol de usuario sin permisos iniciales

    }

    private Set<Permission> scanPermissionsFromControllers() {
        Set<Permission> permissions = new HashSet<>();

        // Obtener todos los beans con la anotación @RestController
        String[] controllerBeans = applicationContext.getBeanNamesForAnnotation(RestController.class);

        for (String beanName : controllerBeans) {
            Object controller = applicationContext.getBean(beanName);
            Class<?> controllerClass = controller.getClass();

            // Obtener el nombre del controlador
            String controllerName = controllerClass.getSimpleName().replace("Controller", "").toLowerCase();

            // Escanear los métodos públicos del controlador
            for (Method method : controllerClass.getMethods()) {
                // Excluir métodos relacionados con Swagger/OpenAPI
                if (controllerName.contains("swagger") || controllerName.contains("auth")|| controllerName.contains("openapi")) {
                    continue;
                }

                // Buscar métodos con anotaciones de mapeo de Spring (como @GetMapping, @PostMapping, etc.)
                RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
                GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
                PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
                PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);
                DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);

                if (requestMapping != null || getMapping != null || postMapping != null || putMapping != null || deleteMapping != null) {
                    // Nombre de la acción, basada en el nombre del método
                    String actionName = method.getName().toLowerCase();

                    // Crear el nombre del permiso en el formato {controlador}:{accion}
                    String permissionName = controllerName + ":" + actionName;

                    // Crear y guardar el permiso si no existe
                    Permission permission = permissionRepository.findByPermissionName(permissionName)
                            .orElseGet(() -> {
                                Permission newPermission = new Permission();
                                newPermission.setPermissionName(permissionName);
                                newPermission.setActive(true);
                                return permissionRepository.save(newPermission);
                            });
                    permissions.add(permission);
                }
            }
        }
        return permissions;
    }

    private void createRoleWithPermissions(String roleName, Set<Permission> permissions) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(roleName);
                    newRole.setIsActive(true);
                    newRole.setPermissions(permissions);
                    return roleRepository.save(newRole);
                });

        // Si el rol ya existe y es "Admin", asegúrate de que tenga todos los permisos
        if (roleName.equals("Admin")) {
            role.setPermissions(permissions);
            roleRepository.save(role);
        }
    }

}
