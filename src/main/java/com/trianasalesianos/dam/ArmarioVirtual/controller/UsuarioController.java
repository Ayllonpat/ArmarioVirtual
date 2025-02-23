package com.trianasalesianos.dam.ArmarioVirtual.controller;

import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.CreateUsuarioDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.GetUsuarioDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.GetAdminDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.GetClienteDto;
import com.trianasalesianos.dam.ArmarioVirtual.model.Admin;
import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.service.UsuarioService;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/crear/{tipoUsuario}")
    public ResponseEntity<?> crearUsuario(@RequestBody CreateUsuarioDto createUsuarioDto,
                                          @RequestParam String tipoUsuario) {
        Usuario usuario = usuarioService.crearUsuario(createUsuarioDto, tipoUsuario);

        if (usuario instanceof Admin) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(GetAdminDto.from((Admin) usuario));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetClienteDto.from((Cliente) usuario));

    }
}
