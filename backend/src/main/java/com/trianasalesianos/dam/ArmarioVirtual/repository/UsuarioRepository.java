package com.trianasalesianos.dam.ArmarioVirtual.repository;

import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsername(String username);

    @Query("""
       SELECT c
         FROM Cliente c
   JOIN c.seguidos s
        WHERE s.id = :userId
    """)
    Page<Cliente> findFollowersById(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
       SELECT c
         FROM Cliente c
   JOIN c.seguidores s
        WHERE s.id = :userId
    """)
    Page<Cliente> findFollowingById(@Param("userId") UUID userId, Pageable pageable);
}
