package com.trianasalesianos.dam.ArmarioVirtual.dto.prenda;

import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import com.trianasalesianos.dam.ArmarioVirtual.model.TipoPrenda;
import com.trianasalesianos.dam.ArmarioVirtual.model.Visibilidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record CreatePrendaDto(
        @NotBlank(message = "{error.nombre.blank}")
        @Size(max = 150, message = "{error.nombre.size}")
        String nombre,

        @NotBlank(message = "{error.imagen.blank}")
        String imagen,

        @Size(max = 50, message = "{error.color.size}")
        String color,

        @Size(max = 50, message = "{error.talla.size}")
        String talla,

        @Size(max = 255, message = "{error.enlaceCompra.size}")
        String enlaceCompra,

        GetTipoPrendaDto tipoPrenda,

        Visibilidad visibilidad,

        List<Long> tagIds
) {
    public Prenda toPrenda(Cliente cliente, TipoPrenda tipoPrenda) {
        return Prenda.builder()
                .nombre(nombre)
                .imagen(imagen)
                .color(color)
                .talla(talla)
                .enlaceCompra(enlaceCompra)
                .tipoPrenda(tipoPrenda)
                .cliente(cliente)
                .fechaPublicacion(LocalDateTime.now())
                .visibilidad(this.visibilidad != null ? this.visibilidad : Visibilidad.PRIVADO)
                .build();
    }
}
