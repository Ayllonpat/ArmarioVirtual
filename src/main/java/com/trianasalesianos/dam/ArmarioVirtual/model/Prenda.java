package com.trianasalesianos.dam.ArmarioVirtual.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Prenda {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 150)
    private String nombre;

    private String imagen;

    private String color;

    private String talla;

    private String enlaceCompra;

    @Enumerated(EnumType.STRING)
    private Visibilidad visibilidad;

    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    private LocalDateTime fechaPublicacion;

    @ManyToOne
    private TipoPrenda tipoPrenda;

    //private int likes;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
