package com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto;

import com.trianasalesianos.dam.ArmarioVirtual.model.Conjunto;

public record GetConjuntoDto(
        String id,
        String nombre,
        String imagen,
        String fechaPublicacion,
        String visibilidad,
        String clienteId
) {
    public static GetConjuntoDto from(Conjunto conjunto) {
        return new GetConjuntoDto(
                conjunto.getId().toString(),
                conjunto.getNombre(),
                conjunto.getImagen(),
                conjunto.getFechaPublicacion().toString(),
                conjunto.getVisibilidad().toString(),
                conjunto.getCliente().getId().toString()
        );
    }
}
