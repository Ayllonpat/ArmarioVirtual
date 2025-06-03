package com.trianasalesianos.dam.ArmarioVirtual.controller;

import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.*;
import com.trianasalesianos.dam.ArmarioVirtual.model.Admin;
import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.ActivationToken;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.service.ActivationTokenService;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.service.JwtService;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.refresh.RefreshToken;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.refresh.RefreshTokenRequest;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.refresh.RefreshTokenService;
import com.trianasalesianos.dam.ArmarioVirtual.service.UsuarioService;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuario",
        description = "Controlador de usuarios, para poder realizar todas sus operaciones de gestión")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final ActivationTokenService activationTokenService;
    private final UsuarioRepository usuarioRepository;

    @Operation(summary = "Un usuario Admin puede crear todo tipo de usuarios pero clientes" +
            "solo pueden crear clientes")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "El usurname o email ya esta repetido",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                   
                                                                        {
                                                                            "type": "about:blank",
                                                                            "title": "Username en uso",
                                                                            "status": 400,
                                                                            "detail": "El nombre de usuario ya está en uso.",
                                                                            "instance": "/api/usuarios/crear/cliente"
                                                                        }
                                                                        
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "201",
                            description = "Se ha creado el usuario ",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Usuario.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                        {
                                                                             "id": "6083fff2-c43a-4a98-a582-2e572f9db15c",
                                                                             "nombre": "Admin Master",
                                                                             "username": "adminmaster9",
                                                                             "email": "admin9@example.com",
                                                                             "enable": false,
                                                                             "activo": true,
                                                                             "prendas": [],
                                                                             "conjuntos": [],
                                                                             "favoritoPrendas": [],
                                                                             "favoritoConjunto": [],
                                                                             "seguidores": [],
                                                                             "seguidos": []
                                                                         }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PostMapping("/crear/{tipoUsuario}")
    public ResponseEntity<?> crearUsuario(@RequestBody CreateUsuarioDto createUsuarioDto,
                                          @PathVariable String tipoUsuario) {
        Usuario usuario = usuarioService.crearUsuario(createUsuarioDto, tipoUsuario);

        if (usuario instanceof Admin) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(GetAdminDto.from((Admin) usuario));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetClienteDto.from((Cliente) usuario));

    }

    @Operation(summary = "Permite a un usuario autenticarse y obtener un token de acceso y un token de refresco.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "401",
                            description = "Las credenciales proporcionadas son incorrectas o el usuario no existe.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProblemDetail.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                    {
                                                                        "type": "about:blank",
                                                                        "title": "Unauthorized",
                                                                        "status": 401,
                                                                        "detail": "Bad credentials",
                                                                        "instance": "/api/usuarios/auth/login"
                                                                    }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "201",
                            description = "El usuario ha iniciado sesión correctamente y se han generado los tokens.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserResponse.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                    {
                                                                        "id": "6083fff2-c43a-4a98-a582-2e572f9db15c",
                                                                        "username": "adminmaster9",
                                                                        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
                                                                        "refreshToken": "a5119a8d-d758-4d46-8cce-624ef3a8ebff"
                                                                    }
                                                                    """
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        Usuario user = (Usuario) authentication.getPrincipal();

        if (!user.getEnable()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cuenta no activada. Revisa tu correo.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user, accessToken, refreshToken.getToken()));
    }


    @PostMapping("/auth/refresh/token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest req)  {
        String token = req.refreshToken();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenService.refreshToken(token));

    }

    @Operation(summary = "Activa la cuenta de un usuario mediante un token enviado por correo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta activada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @GetMapping("/auth/activate/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String token) {
        Optional<ActivationToken> activationToken = activationTokenService.findByToken(token);

        if (activationToken.isEmpty() || activationToken.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido o expirado.");
        }

        Usuario usuario = activationToken.get().getUsuario();
        usuario.setEnable(true);
        usuarioRepository.save(usuario);
        activationTokenService.deleteToken(activationToken.get());

        return ResponseEntity.ok("Cuenta activada exitosamente.");
    }

    @Operation(summary = "Permite obtener la información de un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado exitosamente"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                                    {
                                                                        "id": "6083fff2-c43a-4a98-a582-2e572f9db15c",
                                                                        "username": "adminmaster9",
                                                                        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
                                                                        "refreshToken": "a5119a8d-d758-4d46-8cce-624ef3a8ebff"
                                                                    }
                                                                    """
                                            )
                                    }
                            )
                    }
            )
    })
    @GetMapping("/me")
    public ResponseEntity<?> getUsuarioAutenticado() {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        if (usuario instanceof Admin) {
            return ResponseEntity.ok(GetAdminDto.from((Admin) usuario));
        }

        return ResponseEntity.ok(GetClienteDto.from((Cliente) usuario));
    }
    @Operation(summary = "Permite a un cliente seguir a otro cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ahora sigues a ese usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> follow(@PathVariable UUID id) {
        usuarioService.follow(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Permite a un cliente dejar de seguir a otro cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Has dejado de seguir a ese usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{id}/unfollow")
    public ResponseEntity<Void> unfollow(@PathVariable UUID id) {
        usuarioService.unfollow(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener lista de seguidores de un cliente")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de seguidores obtenida exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetClienteDto.class)
            )
    )
    @GetMapping("/{id}/seguidores")
    public ResponseEntity<List<GetClienteDto>> getSeguidores(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.getSeguidores(id));
    }

    @Operation(summary = "Obtener lista de seguidos de un cliente")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de seguidos obtenida exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetClienteDto.class)
            )
    )
    @GetMapping("/{id}/seguidos")
    public ResponseEntity<List<GetClienteDto>> getSeguidos(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.getSeguidos(id));
    }

}
