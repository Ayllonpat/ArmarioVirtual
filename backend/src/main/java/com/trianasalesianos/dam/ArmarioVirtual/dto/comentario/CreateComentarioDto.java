package com.trianasalesianos.dam.ArmarioVirtual.dto.comentario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateComentarioDto(
        @NotBlank(message = "{error.contenido.blank}")
        @Size(max = 500, message = "{error.contenido.size}")
        String contenido
) { }
