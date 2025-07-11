package com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto;

import com.trianasalesianos.dam.ArmarioVirtual.model.Conjunto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.GetPrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.tag.GetTagDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.GetClientePrendasDto;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record GetConjuntoDto(
        Long id,
        String nombre,
        String imagenUrl,
        LocalDateTime fechaPublicacion,
        List<GetPrendaDto> prendas,
        String visibilidad,
        GetClientePrendasDto cliente,
        List<GetTagDto> tags
) {
    public static GetConjuntoDto from(Conjunto c) {
        String url = c.getImagen() == null
                ? null
                : ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(c.getImagen())
                .toUriString();

        return new GetConjuntoDto(
                c.getId(),
                c.getNombre(),
                url,
                c.getFechaPublicacion(),
                c.getPrendas().stream()
                        .map(GetPrendaDto::from)
                        .collect(Collectors.toList()),
                c.getVisibilidad().toString(),
                GetClientePrendasDto.from(c.getCliente()),
                c.getTags().stream()
                        .map(GetTagDto::from)
                        .collect(Collectors.toList())
        );
    }
}
