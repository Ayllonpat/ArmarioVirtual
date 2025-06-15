package com.trianasalesianos.dam.ArmarioVirtual.repository;

import com.trianasalesianos.dam.ArmarioVirtual.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByNombre(String nombre);
}