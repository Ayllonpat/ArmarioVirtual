package com.trianasalesianos.dam.ArmarioVirtual.security.jwt.refresh;

import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository userRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh.duration}")
    private int durationInMinutes;

    public RefreshToken create(Usuario user) {
        refreshTokenRepository.deleteByUser(user);
        return refreshTokenRepository.save(
          RefreshToken.builder()
                  .user(user)
                  .expireAt(Instant.now().plusSeconds(durationInMinutes*60))
                  .build()
        );
    }

    public RefreshToken verify(RefreshToken refreshToken) {
        if (refreshToken.getExpireAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Token de refresco caducado. Por favor, vuelva a loguearse");
        }

        return refreshToken;

    }

    public com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.UserResponse refreshToken(String token) {

        return refreshTokenRepository.findById(UUID.fromString(token))
                .map(this::verify)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateAccessToken(user);
                    RefreshToken refreshedToken = this.create(user);
                    return com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.UserResponse.of(user, accessToken, refreshedToken.getToken());
                })
                .orElseThrow(() -> new RefreshTokenException("No se ha podido refrescar el token. Por favor, vuelva a loguearse"));

    }

    public void deleteByUser(Usuario user) {
        refreshTokenRepository.deleteByUser(user);
    }


}
