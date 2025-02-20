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
public class Tag {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 150)
    private String nombre;

    @ManyToMany
    private List<Prenda> prendas = new ArrayList<>();

    @ManyToMany
    private List<Conjunto> conjuntos = new ArrayList<>();

}
