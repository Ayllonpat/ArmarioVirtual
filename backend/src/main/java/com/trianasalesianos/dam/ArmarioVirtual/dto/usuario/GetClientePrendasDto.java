package com.trianasalesianos.dam.ArmarioVirtual.dto.usuario;

import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;

public record GetClientePrendasDto(
        String id,
        String username
) {
    public static GetClientePrendasDto from(Cliente cliente) {
        return new GetClientePrendasDto(
                cliente.getId().toString(),
                cliente.getUsername()
        );
    }
}
