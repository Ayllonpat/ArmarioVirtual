package com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.service;

import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.ActivationToken;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.ActivationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivationTokenService {

    private final ActivationTokenRepository activationTokenRepository;

    public ActivationToken createToken(Usuario usuario) {
        ActivationToken token = ActivationToken.builder()
                .token(UUID.randomUUID().toString())
                .usuario(usuario)
                .expirationTime(LocalDateTime.now().plusHours(24))
                .build();
        return activationTokenRepository.save(token);
    }

    public Optional<ActivationToken> findByToken(String token) {
        return activationTokenRepository.findByToken(token);
    }

    public void deleteToken(ActivationToken token) {
        activationTokenRepository.delete(token);
    }
}
