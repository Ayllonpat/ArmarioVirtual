package com.trianasalesianos.dam.ArmarioVirtual.dto.usuario;

import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;

public record GetClienteSimpleDto(
        String id,
        String username
) {
    public static GetClienteSimpleDto from(Cliente cliente) {
        return new GetClienteSimpleDto(
                cliente.getId().toString(),
                cliente.getUsername()
        );
    }
}
