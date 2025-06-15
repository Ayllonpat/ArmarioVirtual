package com.trianasalesianos.dam.ArmarioVirtual.dto.usuario;

import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;

public record GetUsuarioDto(
        String id,
        String nombre,
        String username,
        String email,
        boolean enable,
        boolean activo
) {
    public static GetUsuarioDto from(Usuario usuario) {
        return new GetUsuarioDto(
                usuario.getId().toString(),
                usuario.getNombre(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getEnable(),
                usuario.getActivo()
        );
    }
}
