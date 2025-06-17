package com.trianasalesianos.dam.ArmarioVirtual.repository;

import com.trianasalesianos.dam.ArmarioVirtual.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    @Query("""
           SELECT DISTINCT co
             FROM Comentario co
       LEFT JOIN FETCH co.cliente cl
       LEFT JOIN FETCH co.prenda p
       LEFT JOIN FETCH p.tags t
            WHERE co.prenda.id = :prendaId
           """)
    List<Comentario> findByPrendaIdWithAll(@Param("prendaId") Long prendaId);

    @Query("""
           SELECT DISTINCT co
             FROM Comentario co
       LEFT JOIN FETCH co.cliente cl
       LEFT JOIN FETCH co.conjunto cj
       LEFT JOIN FETCH cj.prendas p
       LEFT JOIN FETCH p.tags t
            WHERE co.conjunto.id = :conjuntoId
           """)
    List<Comentario> findByConjuntoIdWithAll(@Param("conjuntoId") Long conjuntoId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Comentario c WHERE c.prenda.id = :prendaId")
    void deleteByPrendaId(@Param("prendaId") Long prendaId);

    @Modifying
    @Query("DELETE FROM Comentario c WHERE c.conjunto.id = :conjuntoId")
    void deleteByConjuntoId(@Param("conjuntoId") Long conjuntoId);

}
