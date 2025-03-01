package com.trianasalesianos.dam.ArmarioVirtual.repository;

import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PrendaRepository extends JpaRepository<Prenda, Long> {

    @Query("""
            SELECT COUNT(p) > 0 
            FROM Prenda p 
            JOIN p.clientesQueDieronLike c 
            WHERE p.id = :prendaId 
            AND c.id = :clienteId
            """)
    boolean existsLikeByCliente(@Param("prendaId") Long prendaId, @Param("clienteId") UUID clienteId);

    @Query("""
            SELECT COUNT(c) 
            FROM Prenda p 
            JOIN p.clientesQueDieronLike c 
            WHERE p.id = :prendaId
            """)
    long countLikes(@Param("prendaId") Long prendaId);
}
