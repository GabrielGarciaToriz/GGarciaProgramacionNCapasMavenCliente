# GGarciaProgramacionNCapasMavenCliente

Cliente Spring Boot (Thymeleaf + JS) que consume APIs de `GGarciaProgramacionNCapasMavenServices`.

## Requisitos

- Java 17
- Maven Wrapper (incluido: `mvnw.cmd`)

## Configuracion de entornos

La app usa perfiles y propiedades para evitar URLs hardcodeadas.

- Perfil por defecto: `dev`
- Propiedad base de servicios: `services.base-url`
- Variable de entorno soportada: `SERVICES_BASE_URL`

### Archivos

- `src/main/resources/application.properties`
  - `spring.profiles.default=dev`
  - `services.base-url=${SERVICES_BASE_URL:http://localhost:8081}`
  - Endpoints `services.endpoints.*`
- `src/main/resources/application-dev.properties`
  - `services.base-url=http://localhost:8081`
- `src/main/resources/application-prod.properties`
  - `services.base-url=${SERVICES_BASE_URL}`

## Endpoints configurables

Definidos en `application.properties` y mapeados en:
`src/main/java/com/digis01/GGarciaProgramacionNCapasMavenCliente/Config/ServicesProperties.java`

- `services.endpoints.usuario=/api/usuario`
- `services.endpoints.rol=/api/rol`
- `services.endpoints.pais=/api/pais`
- `services.endpoints.direccion=/api/direccion`
- `services.endpoints.estado=/api/estado`
- `services.endpoints.municipio=/api/municipio`
- `services.endpoints.colonia=/api/colonia`

## Uso rapido (PowerShell)

### 1) Ejecutar pruebas

```powershell
.\mvnw.cmd -q test
```

### 2) Levantar en desarrollo

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3) Levantar en produccion

```powershell
$env:SERVICES_BASE_URL="https://tu-api-services"
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

## Login y permisos por rol

- Ruta de login del cliente: `http://localhost:8080/login`
- Endpoint de autenticacion backend configurable: `services.endpoints.auth-login` (default: `/api/usuario/login`)
- El cliente reenvia el token Basic de la sesion a los llamados hacia Services.

### Reglas de acceso

- `ROLE_ADMIN`:
  - Alta/edicion sensible (`/usuario/form`, `/usuario/add`, `/usuario/addDirection/**`, `/usuario/cargar/**`)
  - Acciones administrativas visibles en pantalla (estatus, botones de gestion)
- `ROLE_ADMIN` y `ROLE_USER`:
  - Consulta (`/usuario`, `/usuario/detail/**`, `/usuario/buscar`)
- Sin autenticacion:
  - `/login`, recursos estaticos, `/403`

## Notas

- El frontend lee la configuracion de servicios desde meta tags en `src/main/resources/templates/components/head.html`.
- `src/main/resources/templates/Usuario.html` ya construye la URL de cambio de estatus con propiedades (`servicesBaseUrl` + `usuarioEndpoint`).

