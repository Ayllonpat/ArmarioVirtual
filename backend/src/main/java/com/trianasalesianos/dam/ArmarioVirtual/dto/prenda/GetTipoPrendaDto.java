package com.trianasalesianos.dam.ArmarioVirtual.dto.prenda;

import com.trianasalesianos.dam.ArmarioVirtual.model.TipoPrenda;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record GetTipoPrendaDto(
        Long id,
        String nombre,
        String tipoPrendaPadre,
        List<GetTipoPrendaDto> tipoPrendasHijas
) {
    public static GetTipoPrendaDto from(TipoPrenda tipoPrenda) {
        return new GetTipoPrendaDto(
                tipoPrenda.getId(),
                tipoPrenda.getNombre(),
                Optional.ofNullable(tipoPrenda.getTipoPrendaPadre())
                        .map(TipoPrenda::getNombre)
                        .orElse(null),
                tipoPrenda.getTipoPrendasHijas() != null
                        ? tipoPrenda.getTipoPrendasHijas().stream()
                        .map(GetTipoPrendaDto::from)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }
}
