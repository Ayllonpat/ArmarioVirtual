# README.md

## Descripción del proyecto
Armario Virtual es una aplicación full-stack para gestionar de forma digital el armario personal, planificar atuendos y compartir inspiración de moda en una comunidad de usuarios.

## Implementación Backend
El backend está desarrollado en Java con Spring Boot, estructurado en los siguientes paquetes y clases:

- **config**
  - `SecurityConfig`: configuración de Spring Security y JWT  
  - `OpenApiConfig`: documentación automática con Swagger  
  - `WebConfig`: manejo de recursos estáticos y directorios de subida  
  - `PasswordEncoderConfig`: configuración de cifrado de contraseñas  
  - `MailConfig`: configuración de envío de correo para activación de cuentas  

- **controller**
  - `UsuarioController`: registro, autenticación, perfil de usuario, seguimiento  
  - `PrendaController`: CRUD de prendas, gestión de imágenes, likes y filtrado  
  - `ConjuntoController`: CRUD de conjuntos, gestión de imágenes, likes y filtrado  
  - `ComentarioController`: creación y gestión de comentarios en prendas/conjuntos  
  - `TagController`: CRUD de tags y asociación con entidades  
  - `ViewController`: renderización de vistas JSP (login)  

- **dto**
  - Clases `CreateXDto`, `UpdateXDto`, `GetXDto` para cada entidad  
  - `LoginRequest`, `UserResponse`, `RefreshTokenRequest` para autenticación JWT  

- **error**
  - Excepciones personalizadas y manejadores globales (`GlobalExceptionHandler`, `JwtControllerAdvice`)  

- **model**
  - Entidades JPA (`Usuario`, `Cliente`, `Admin`, `Prenda`, `Conjunto`, `Comentario`, `Tag`, `TipoPrenda`, `RefreshToken`, `ActivationToken`)  
  - Enums de soporte (`Role`, `Visibilidad`)  

- **repository**
  - Interfaces Spring Data (`JpaRepository`, `JpaSpecificationExecutor`) con consultas personalizadas (`@Query`)  

- **security**
  - Filtros y manejadores JWT (`JwtAuthenticationFilter`, `JwtAuthenticationEntryPoint`, `JwtAccessDeniedHandler`)  
  - Servicios de token (`JwtService`, `ActivationTokenService`, `RefreshTokenService`)  
  - `CustomUserDetailService` para cargar detalles de usuario  

- **service**
  - Lógica de negocio por entidad (`UsuarioService`, `PrendaService`, `ConjuntoService`, `ComentarioService`, `TagService`)  
  - `EmailService` para notificaciones de activación  

## Implementación Frontend
El frontend se ha desarrollado con **Angular**, siguiendo una arquitectura de componentes y servicios:

- **Módulos**: organizados en características (prendas, conjuntos, usuarios, comentarios)  
- **Componentes**: formularios de login, registro, listado y detalle de prendas y conjuntos, creación de comentarios  
- **Servicios**: consumo de la API REST (autenticación, gestión de entidades, token interceptor)  
- **Rutas**: protección de rutas mediante guards que validan la presencia de JWT en localStorage  

### Ejecución con Docker Compose
1. En el directorio raíz del proyecto, crea un fichero `docker-compose.yml` que defina los servicios de backend, frontend y base de datos.  
2. Ejecuta:
   ```bash
   docker-compose up --build
3. Accede a la aplicación web en http://localhost:4200 y a la API en http://localhost:8080.
4. Para detener los contenedores: `docker-compose down`
