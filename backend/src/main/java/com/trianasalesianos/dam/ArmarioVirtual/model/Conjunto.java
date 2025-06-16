package com.trianasalesianos.dam.ArmarioVirtual.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Conjunto {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 150, nullable = false)
    private String nombre;

    private String imagen;

    private LocalDateTime fechaPublicacion;

    @ManyToMany
    @JoinTable(
            name = "conjunto_prendas",
            joinColumns = @JoinColumn(name = "conjunto_id"),
            inverseJoinColumns = @JoinColumn(name = "prenda_id")
    )
    @Builder.Default
    private List<Prenda> prendas = new ArrayList<>();

    @ManyToMany(mappedBy = "conjuntos")
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Visibilidad visibilidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToMany
    @JoinTable(
            name = "conjunto_likes",
            joinColumns = @JoinColumn(name = "conjunto_id"),
            inverseJoinColumns = @JoinColumn(name = "cliente_id")
    )
    private Set<Cliente> clientesQueDieronLike = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Conjunto conjunto = (Conjunto) o;
        return getId() != null && Objects.equals(getId(), conjunto.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
