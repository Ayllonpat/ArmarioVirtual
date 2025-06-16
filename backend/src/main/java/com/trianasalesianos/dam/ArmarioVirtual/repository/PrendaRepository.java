package com.trianasalesianos.dam.ArmarioVirtual.repository;

import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrendaRepository extends JpaRepository<Prenda, Long>, JpaSpecificationExecutor<Prenda> {

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

    @Query("""
           SELECT DISTINCT p
             FROM Prenda p
       LEFT JOIN FETCH p.tipoPrenda tp
       LEFT JOIN FETCH p.cliente c
       LEFT JOIN FETCH p.tags t
            WHERE p.id = :id
           """)
    Optional<Prenda> findByIdWithAll(@Param("id") Long id);

    @Query("""
           SELECT DISTINCT p
             FROM Prenda p
       LEFT JOIN FETCH p.tipoPrenda tp
       LEFT JOIN FETCH p.cliente c
       LEFT JOIN FETCH p.tags t
           """)
    List<Prenda> findAllWithAll();

    List<Prenda> findByClienteUsername(String username);

}
