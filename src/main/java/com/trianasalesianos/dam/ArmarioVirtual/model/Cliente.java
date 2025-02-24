package com.trianasalesianos.dam.ArmarioVirtual.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class Cliente extends Usuario{

    @OneToMany(mappedBy = "cliente",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Prenda> prendas = new ArrayList<>();

    @OneToMany(mappedBy = "cliente",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Conjunto> conjuntos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "favorito_prendas",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "prenda_id")
    )
    private List<Prenda> favoritoPrendas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "favorito_conjunto",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "conjunto_id")
    )
    private List<Conjunto> favoritoConjunto = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "seguidores",
            joinColumns = @JoinColumn(name = "seguido_id"),
            inverseJoinColumns = @JoinColumn(name = "seguidor_id")
    )
    private List<Cliente> seguidores = new ArrayList<>();

    @ManyToMany(mappedBy = "seguidores")
    private List<Cliente> seguidos = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"));
    }

}
