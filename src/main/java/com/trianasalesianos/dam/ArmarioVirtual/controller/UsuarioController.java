package com.trianasalesianos.dam.ArmarioVirtual.controller;

import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.*;
import com.trianasalesianos.dam.ArmarioVirtual.model.Admin;
import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.JwtService;
import com.trianasalesianos.dam.ArmarioVirtual.service.UsuarioService;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


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

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {


        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),
                                loginRequest.password()
                        )
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario user = (Usuario) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user, accessToken, refreshToken.getToken()));
    }
}
