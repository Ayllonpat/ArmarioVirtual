package com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto;

import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.GetPrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.GetClientePrendasDto;
import com.trianasalesianos.dam.ArmarioVirtual.model.Conjunto;
import com.trianasalesianos.dam.ArmarioVirtual.model.Visibilidad;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record GetConjuntoDto(
        Long id,
        String nombre,
        String imagen,
        LocalDateTime fechaPublicacion,
        List<GetPrendaDto> prendas,
        Visibilidad visibilidad,
        GetClientePrendasDto cliente
) {
    public static GetConjuntoDto from(Conjunto conjunto) {
        return new GetConjuntoDto(
                conjunto.getId(),
                conjunto.getNombre(),
                conjunto.getImagen(),
                conjunto.getFechaPublicacion(),
                conjunto.getPrendas().stream()
                        .map(GetPrendaDto::from)
                        .collect(Collectors.toList()),
                conjunto.getVisibilidad(),
                new GetClientePrendasDto(
                        conjunto.getCliente().getId().toString(),
                        conjunto.getCliente().getUsername()
                )
        );
    }
}
