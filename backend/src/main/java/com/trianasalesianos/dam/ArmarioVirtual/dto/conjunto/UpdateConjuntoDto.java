package com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto;

import com.trianasalesianos.dam.ArmarioVirtual.model.Visibilidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateConjuntoDto(
        @NotBlank(message = "{error.nombre.blank}")
        @Size(max = 150, message = "{error.nombre.size}")
        String nombre,
        String imagen,
        List<Long> prendasIds,
        Visibilidad visibilidad
) {
}