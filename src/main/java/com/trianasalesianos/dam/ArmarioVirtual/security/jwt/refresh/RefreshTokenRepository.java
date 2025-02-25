package com.trianasalesianos.dam.ArmarioVirtual.security.jwt.refresh;

import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface RefreshTokenRepository
    extends JpaRepository<RefreshToken, UUID> {


    @Modifying
    @Transactional
    void deleteByUser(Usuario user);

}
