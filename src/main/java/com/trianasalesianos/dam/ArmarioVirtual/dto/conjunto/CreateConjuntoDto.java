package com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto;

import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.model.Conjunto;
import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import com.trianasalesianos.dam.ArmarioVirtual.model.Visibilidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record CreateConjuntoDto(
        @NotBlank(message = "{error.nombre.blank}")
        @Size(max = 150, message = "{error.nombre.size}")
        String nombre,

        String imagen,

        List<Long> prendasIds,

        Visibilidad visibilidad
) {
    public Conjunto toConjunto(List<Prenda> prendas, Cliente cliente) {
        return Conjunto.builder()
                .nombre(this.nombre)
                .imagen(this.imagen)
                .fechaPublicacion(LocalDateTime.now())
                .prendas(prendas)
                .cliente(cliente)
                .visibilidad(this.visibilidad != null ? this.visibilidad : Visibilidad.PRIVADO)
                .build();
    }
}
