package com.trianasalesianos.dam.ArmarioVirtual.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTagDto(
        @NotBlank(message = "{error.nombre.blank}")
        @Size(max = 150, message = "{error.nombre.size}")
        String nombre
) { }
