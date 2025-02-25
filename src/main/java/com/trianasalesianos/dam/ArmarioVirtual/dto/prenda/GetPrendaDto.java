package com.trianasalesianos.dam.ArmarioVirtual.dto.prenda;

import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;

public record GetPrendaDto(
        String id,
        String nombre,
        String imagen,
        String color,
        String talla,
        String enlaceCompra,
        String visibilidad,
        String tipoPrendaId,
        String clienteId
) {
    public static GetPrendaDto from(Prenda prenda) {
        return new GetPrendaDto(
                prenda.getId().toString(),
                prenda.getNombre(),
                prenda.getImagen(),
                prenda.getColor(),
                prenda.getTalla(),
                prenda.getEnlaceCompra(),
                prenda.getVisibilidad().toString(),
                prenda.getTipoPrenda().getId().toString(),
                prenda.getCliente().getId().toString()
        );
    }
}
