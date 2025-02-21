package com.trianasalesianos.dam.ArmarioVirtual.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Column(length = 150, nullable = false)
    private String nombre;

    private String imagen;

    private String color;

    private String talla;

    private String enlaceCompra;

    @Enumerated(EnumType.STRING)
    private Visibilidad visibilidad;

    @ManyToMany(mappedBy = "prendas")
    private List<Tag> tags = new ArrayList<>();

    private LocalDateTime fechaPublicacion;

    @ManyToOne
    @JoinColumn(name = "tipo_prenda_id", nullable = false)
    private TipoPrenda tipoPrenda;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Prenda prenda = (Prenda) o;
        return getId() != null && Objects.equals(getId(), prenda.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
