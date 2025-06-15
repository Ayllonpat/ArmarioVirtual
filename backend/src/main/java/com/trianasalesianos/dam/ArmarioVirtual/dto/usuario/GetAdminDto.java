package com.trianasalesianos.dam.ArmarioVirtual.dto.usuario;

import com.trianasalesianos.dam.ArmarioVirtual.model.Admin;

public record GetAdminDto(
        String id,
        String nombre,
        String username,
        String email,
        boolean enable,
        boolean activo
) {
    public static GetAdminDto from(Admin admin) {
        return new GetAdminDto(
                admin.getId().toString(),
                admin.getNombre(),
                admin.getUsername(),
                admin.getEmail(),
                admin.getEnable(),
                admin.getActivo()
        );
    }
}
