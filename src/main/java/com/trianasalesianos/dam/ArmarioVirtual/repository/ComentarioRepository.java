package com.trianasalesianos.dam.ArmarioVirtual.repository;

import com.trianasalesianos.dam.ArmarioVirtual.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}
