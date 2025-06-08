package com.trianasalesianos.dam.ArmarioVirtual.dto.prenda;

import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.GetClientePrendasDto;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;



public record GetPrendaDto(
        String nombre,
        String imagenUrl,      // ahora contiene la URL pública
        String color,
        String talla,
        String enlaceCompra,
        String visibilidad,
        String tipoPrendaId,
        GetClientePrendasDto cliente
) {
    public static GetPrendaDto from(Prenda p) {

        String url = p.getImagen() == null
                ? null
                : ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/prendas/")
                .path(p.getImagen())
                .toUriString();

        return new GetPrendaDto(
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
