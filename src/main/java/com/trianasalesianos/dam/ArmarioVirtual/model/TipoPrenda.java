package com.trianasalesianos.dam.ArmarioVirtual.model;

import jakarta.persistence.*;
import lombok.*;

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
public class TipoPrenda {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 150)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "tipo_prenda_padre_id")
    private TipoPrenda tipoPrendaPadre;

    private List<TipoPrenda> tipoPrendasHijas = new ArrayList<>();

    @OneToMany(

    )
    private List<Prenda> prendas = new ArrayList<>();

}
