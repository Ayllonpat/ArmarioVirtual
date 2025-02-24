package com.trianasalesianos.dam.ArmarioVirtual.repository;

import com.trianasalesianos.dam.ArmarioVirtual.model.TipoPrenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoPrendaRepository extends JpaRepository<TipoPrenda, Long> {

    TipoPrenda findByNombre(String nombre);

}
