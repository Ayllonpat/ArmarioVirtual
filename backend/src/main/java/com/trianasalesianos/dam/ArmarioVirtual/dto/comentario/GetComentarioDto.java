package com.trianasalesianos.dam.ArmarioVirtual.dto.comentario;

import com.trianasalesianos.dam.ArmarioVirtual.model.Comentario;
import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.GetClientePrendasDto;

import java.time.LocalDateTime;

public record GetComentarioDto(
        Long id,
        String contenido,
        LocalDateTime fechaPublicacion,
        GetClientePrendasDto cliente
) {
    public static GetComentarioDto from(Comentario c) {
        return new GetComentarioDto(
                c.getId(),
                c.getContenido(),
                c.getFechaPublicacion(),
                new GetClientePrendasDto(
                        c.getCliente().getId().toString(),
                        c.getCliente().getUsername()
                )
        );
    }
}
