package com.trianasalesianos.dam.ArmarioVirtual.dto.usuario;

import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.GetPrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.GetConjuntoDto;

import java.util.List;
import java.util.stream.Collectors;

public record GetClienteDto(
        String id,
        String nombre,
        String username,
        String email,
        boolean enable,
        boolean activo,
        List<GetPrendaDto> prendas,
        List<GetConjuntoDto> conjuntos,
        List<GetPrendaDto> favoritoPrendas,
        List<GetConjuntoDto> favoritoConjunto,
        List<GetClienteDto> seguidores,
        List<GetClienteDto> seguidos
) {
    public static GetClienteDto from(Cliente cliente) {
        List<GetPrendaDto> prendas = cliente.getPrendas().stream()
                .map(GetPrendaDto::from)
                .collect(Collectors.toList());

        List<GetConjuntoDto> conjuntos = cliente.getConjuntos().stream()
                .map(GetConjuntoDto::from)
                .collect(Collectors.toList());

        List<GetPrendaDto> favoritoPrendas = cliente.getFavoritoPrendas().stream()
                .map(GetPrendaDto::from)
                .collect(Collectors.toList());

        List<GetConjuntoDto> favoritoConjunto = cliente.getFavoritoConjunto().stream()
                .map(GetConjuntoDto::from)
                .collect(Collectors.toList());

        List<GetClienteDto> seguidores = cliente.getSeguidores().stream()
                .map(GetClienteDto::from)
                .collect(Collectors.toList());

        List<GetClienteDto> seguidos = cliente.getSeguidos().stream()
                .map(GetClienteDto::from)
                .collect(Collectors.toList());

        return new GetClienteDto(
                cliente.getId().toString(),
                cliente.getNombre(),
                cliente.getUsername(),
                cliente.getEmail(),
                cliente.getEnable(),
                cliente.getActivo(),
                prendas,
                conjuntos,
                favoritoPrendas,
                favoritoConjunto,
                seguidores,
                seguidos
        );
    }
}
