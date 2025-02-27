package com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso;

import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivationToken {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    private LocalDateTime expirationTime;
}

