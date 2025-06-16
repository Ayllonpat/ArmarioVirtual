package com.trianasalesianos.dam.ArmarioVirtual.repository;

import com.trianasalesianos.dam.ArmarioVirtual.model.Conjunto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface ConjuntoRepository extends JpaRepository<Conjunto, Long>, JpaSpecificationExecutor<Conjunto> {

    @Query("""
           SELECT DISTINCT c
             FROM Conjunto c
       LEFT JOIN FETCH c.prendas p
       LEFT JOIN FETCH p.tags t
       LEFT JOIN FETCH c.cliente cl
       LEFT JOIN FETCH c.tags ct
            WHERE c.id = :id
           """)
    Optional<Conjunto> findByIdWithAll(@Param("id") Long id);

    @Query("""
           SELECT DISTINCT c
             FROM Conjunto c
       LEFT JOIN FETCH c.prendas p
       LEFT JOIN FETCH p.tags t
       LEFT JOIN FETCH c.cliente cl
       LEFT JOIN FETCH c.tags ct
           """)
    List<Conjunto> findAllWithAll();

    List<Conjunto> findByClienteUsername(String username);


}
