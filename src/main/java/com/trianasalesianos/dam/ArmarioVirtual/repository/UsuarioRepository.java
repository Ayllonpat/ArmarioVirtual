package com.trianasalesianos.dam.ArmarioVirtual.repository;

import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);

}
