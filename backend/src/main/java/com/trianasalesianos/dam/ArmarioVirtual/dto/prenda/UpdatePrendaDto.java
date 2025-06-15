// src/main/java/com/trianasalesianos/dam/ArmarioVirtual/dto/prenda/UpdatePrendaDto.java
package com.trianasalesianos.dam.ArmarioVirtual.dto.prenda;

import com.trianasalesianos.dam.ArmarioVirtual.model.Visibilidad;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

public record UpdatePrendaDto(
        @NotBlank(message = "{error.nombre.blank}")
        @Size(max = 150, message = "{error.nombre.size}")
        String nombre,

        String imagen,

        @Size(max = 50, message = "{error.color.size}")
        String color,

        @Size(max = 50, message = "{error.talla.size}")
        String talla,

        @Size(max = 255, message = "{error.enlaceCompra.size}")
        String enlaceCompra,

        Long tipoPrendaId,

        Visibilidad visibilidad
) {
}