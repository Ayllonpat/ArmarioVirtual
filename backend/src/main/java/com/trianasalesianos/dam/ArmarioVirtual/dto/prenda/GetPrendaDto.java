package com.trianasalesianos.dam.ArmarioVirtual.dto.prenda;

import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.GetClientePrendasDto;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public record GetPrendaDto(
        Long id,
        String nombre,
        String imagenUrl,
        String color,
        String talla,
        String enlaceCompra,
        String visibilidad,
        String tipoPrendaId,
        GetClientePrendasDto cliente
) {public static GetPrendaDto from(Prenda p) {
    String url = p.getImagen() == null
            ? null
            : ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/images/")
            .path(p.getImagen())
            .toUriString();

    return new GetPrendaDto(
            p.getId(),
            p.getNombre(),
            url,
            p.getColor(),
            p.getTalla(),
            p.getEnlaceCompra(),
            p.getVisibilidad().toString(),
            p.getTipoPrenda().getId().toString(),
            GetClientePrendasDto.from(p.getCliente())
    );
}
}