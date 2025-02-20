package com.trianasalesianos.dam.ArmarioVirtual.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
public class Cliente extends Usuario{

    @OneToMany(
            mappedBy = "cliente",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Prenda> prendas = new ArrayList<>();

    @OneToMany
    private List<Conjunto> conjuntos= new ArrayList<>();

    @ManyToMany
    private List<Prenda> favoritoPrendas = new ArrayList<>();

    @ManyToMany
    private List<Conjunto> favoritoConjunto = new ArrayList<>();

    @ManyToMany
    private List<Usuario> seguidores = new ArrayList<>();

    @ManyToMany
    private List<Usuario> seguidos = new ArrayList<>();


}
