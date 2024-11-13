
# Proyecto API de Autenticación y Autorización con JWT

Este proyecto es una API backend en Java, construida con Spring Boot, para la gestión de autenticación y autorización basada en tokens JWT (JSON Web Tokens). Proporciona un sistema seguro para el registro, login, y control de acceso mediante roles y permisos personalizados, ideal para aplicaciones que requieren una administración de usuarios y permisos detallada.

## Características
- **JWT Autenticación**: Uso de tokens de acceso y refresco para mantener sesiones seguras.
- **Roles y Permisos**: Gestión granular de permisos mediante asignación de roles.
- **Swagger Integrado**: Interfaz de prueba para endpoints, configurada para admitir autenticación Bearer.
- **Procedimientos Almacenados**: Uso de SQL Server para procesos eficientes y validación de datos.

## Endpoints Principales

### Autenticación
1. **Registro de Usuario**
   - **Endpoint**: `/api/auth/register`
   - **Método**: `POST`
   - **Descripción**: Permite registrar un nuevo usuario con email, nombre de usuario y contraseña.
   
2. **Inicio de Sesión**
   - **Endpoint**: `/api/auth/login`
   - **Método**: `POST`
   - **Descripción**: Genera tokens de acceso y refresco para un usuario autenticado.

3. **Refrescar Token**
   - **Endpoint**: `/api/auth/refresh-token`
   - **Método**: `POST`
   - **Descripción**: Proporciona un nuevo token de acceso usando un refresh token válido.

4. **Logout**
   - **Endpoint**: `/api/auth/logout`
   - **Método**: `POST`
   - **Descripción**: Cierra sesión eliminando las cookies de autenticación.

## Configuración de Seguridad

- **JWT**: Configurado con clave secreta y expiración para tokens de acceso y refresh.
- **Swagger**: Integrado para aceptar Bearer tokens en el header de cada petición.
- **Filtro de Autorización**: Verifica permisos según el rol del usuario y el endpoint solicitado.

## Ejecución

1. Clona este repositorio.
2. Configura las variables de entorno en `application.properties`.
3. Ejecuta la aplicación con Maven o desde tu IDE preferido.

## Requerimientos

- Java 11 o superior.
- Spring Boot.
- SQL Server para almacenamiento de datos.
- Maven para la construcción del proyecto.

## Notas Adicionales

Este proyecto está pensado como una plantilla para aplicaciones que requieren autenticación avanzada y control detallado de permisos. Revisa y ajusta los permisos y roles en `PermissionService` y `RoleService` para adaptarlos a tus necesidades específicas.

