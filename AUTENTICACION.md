# Guía de Login y Control de Acceso por Rol

## Resumen de cambios implementados

Se ha integrado **Spring Security** con autenticación contra el backend de servicios y autorización granular por roles.

## Arquitectura de seguridad

### 1. **Capa de autenticación**

- **`BackendAuthService`**: Valida credenciales contra `POST /api/usuario/login` (configurable).
  - Normaliza roles de texto libre (ej: "Administrador" → "ROLE_ADMIN").
  - Genera token Basic Auth para propagación a servicios backend.

- **`BackendAuthenticationProvider`**: Implementa `AuthenticationProvider` de Spring Security.
  - Convierte credenciales en `AppUserPrincipal` (UserDetails).

- **`AppUserPrincipal`**: UserDetails que almacena:
  - Nombre de usuario
  - Nombre completo (para mostrar en UI)
  - Token Authorization header (Basic Auth)
  - Autoridades (roles)

### 2. **Configuración de seguridad (`SecurityConfig`)**

**Rutas públicas (sin autenticación):**
- `/login` - formulario de acceso
- `/403` - página de acceso denegado
- `/css/**`, `/js/**`, `/img/**` - recursos estáticos
- `/error` - páginas de error

**Rutas autenticadas (cualquier rol):**
- `GET /usuario` - listar usuarios
- `GET /usuario/detail/**` - ver detalles de usuario
- `POST /usuario/buscar` - buscar usuarios

**Rutas solo ADMIN:**
- `GET /usuario/form` - formulario crear usuario
- `POST /usuario/add` - crear usuario
- `/usuario/addDirection/**` - agregar dirección a usuario
- `/usuario/cargar/**` - carga masiva de usuarios

### 3. **Propagación de credenciales a backend**

En `UsuarioController`, cada llamada REST a Services incluye el header `Authorization`:

```java
private HttpHeaders authHeaders() {
    HttpHeaders headers = new HttpHeaders();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof AppUserPrincipal principal) {
        headers.set(HttpHeaders.AUTHORIZATION, principal.getApiAuthorizationHeader());
    }
    return headers;
}
```

Esto asegura que el backend pueda validar autorización en tiempo de request.

### 4. **Inyección de contexto en vistas**

`UserSessionModelAdvice` proporciona al modelo Thymeleaf:
- `${loggedUserName}` - nombre completo del usuario autenticado
- `${isAdmin}` - flag booleano para mostrar/ocultar controles administrativos

## Flujo de uso

### Acceso sin autenticación

```
Usuario intenta acceder a /usuario
    ↓
Spring Security lo redirige a /login
    ↓
Usuario completa formulario (username + password)
    ↓
BackendAuthService valida contra backend
    ↓
Si válido → crea sesión y redirige a /usuario
Si inválido → muestra error en /login
```

### Acceso a recurso protegido

```
Usuario autenticado intenta POST /usuario/add
    ↓
SecurityFilterChain valida rol (ROLE_ADMIN)
    ↓
Si cumple → UsuarioController procesa + propaga Auth header
Si no → redirige a /403
```

## Configuración en properties

### `application.properties` (base)
```ini
services.endpoints.auth-login=/api/usuario/login
```

### `application-dev.properties`
```ini
services.base-url=http://localhost:8081
```

### `application-prod.properties`
```ini
services.base-url=${SERVICES_BASE_URL}
# Define SERVICES_BASE_URL en variable de entorno del servidor
```

## Mapeo de roles

El cliente normaliza nombres de rol recibidos del backend:

| Entrada (backend) | Salida (cliente) |
|---|---|
| `"Administrador"` | `ROLE_ADMIN` |
| `"Admin"` | `ROLE_ADMIN` |
| `"Usuario"` | `ROLE_USUARIO` |
| `"User"` | `ROLE_USER` |
| Cualquier otro | `ROLE_` + nombre en MAYÚSCULAS |

> Usa la convención `ROLE_` de Spring Security automáticamente.

## Vistas modificadas

- **`login.html`** (nueva)
  - Formulario username/password
  - Muestra errores de autenticación
  - Enlace a logout

- **`error/403.html`** (nueva)
  - Página de acceso denegado
  - Botón para volver a inicio

- **`Usuario.html`** (modificada)
  - Barra de sesión: muestra usuario + botón logout
  - "Cargar Usuarios" visible solo si `${isAdmin}`
  - Columna "Estatus" visible solo si `${isAdmin}`
  - Botón "Eliminar" visible solo si `${isAdmin}`

## Ejemplo: Agregar nueva ruta protegida

Para proteger una nueva ruta por rol:

### 1. En `SecurityConfig.java`

```java
.requestMatchers("/mi-ruta/**").hasRole("ADMIN")
```

### 2. En `UsuarioController`

Agregar helper method para credenciales:

```java
ResponseEntity<...> response = restTemplate.exchange(
    url,
    HttpMethod.GET,
    authorizedEmptyEntity(),  // ← Incluye Auth header
    ...
);
```

### 3. En vista (opcional)

```html
<div th:if="${isAdmin}">
    <!-- Mostrar solo si es ADMIN -->
</div>
```

## Pruebas de funcionamiento

### Test 1: Login exitoso

```powershell
# Levantar app
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# Navegar a http://localhost:8080/usuario
# → Redirige a /login

# Completar con credenciales válidas del backend
# → Acceso permitido, ve lista de usuarios
```

### Test 2: Acceso denegado (no admin)

```
Usuario con role ROLE_USER intenta acceder a /usuario/form
→ Se redirige a /403
→ No puede crear usuarios
```

### Test 3: Logout

```
Usuario hace clic en "Cerrar sesion"
→ POST /logout
→ Sesión destruida
→ Redirige a /login?logout
```

## Troubleshooting

### Error: "Credenciales invalidas" en login

**Causa:** `services.endpoints.auth-login` no coincide con endpoint real del backend.

**Solución:**
1. Verificar endpoint en `application.properties`:
   ```ini
   services.endpoints.auth-login=/api/usuario/login
   ```
2. Confirmar que backend tiene ese endpoint y retorna `Usuario` o `Result<Usuario>`.

### Error: 403 sin motivo aparente

**Causa:** Rol retornado por backend no normaliza a `ROLE_ADMIN`.

**Solución:**
1. Verificar log de normalización en `BackendAuthService.normalizeRole()`.
2. Agregar case en SecurityConfig si rol es custom.

### Usuario ve UI de admin pero operación falla

**Causa:** Backend no propaga credenciales o token expiró.

**Solución:**
1. Verificar que `UsuarioController` usa `authorizedEmptyEntity()` / `authorizedEntity()`.
2. Confirmar que backend valida header `Authorization`.

---

**Última actualización:** 2026-03-26

