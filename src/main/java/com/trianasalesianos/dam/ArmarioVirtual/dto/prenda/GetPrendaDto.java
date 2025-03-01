package com.trianasalesianos.dam.ArmarioVirtual.dto.prenda;

import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.GetClientePrendasDto;
import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;

public record GetPrendaDto(
        String nombre,
        String imagen,
        String color,
        String talla,
        String enlaceCompra,
        String visibilidad,
        String tipoPrendaId,
        GetClientePrendasDto cliente
) {
    public static GetPrendaDto from(Prenda prenda) {
        return new GetPrendaDto(
                prenda.getNombre(),
                prenda.getImagen(),
                prenda.getColor(),
                prenda.getTalla(),
                prenda.getEnlaceCompra(),
                prenda.getVisibilidad().toString(),
                prenda.getTipoPrenda().getId().toString(),
                GetClientePrendasDto.from(prenda.getCliente())
        );
    }
}
