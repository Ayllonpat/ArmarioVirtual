package com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, String> {
    Optional<ActivationToken> findByToken(String token);
}
