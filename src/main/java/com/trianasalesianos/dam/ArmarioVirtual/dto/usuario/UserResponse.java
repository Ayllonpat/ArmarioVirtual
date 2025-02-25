package com.trianasalesianos.dam.ArmarioVirtual.dto.usuario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;

import java.util.UUID;

public record UserResponse(UUID id,
                           String username,
                           @JsonInclude(JsonInclude.Include.NON_NULL)
                           String token,
                           @JsonInclude(JsonInclude.Include.NON_NULL)
                           String refreshToken) {
    public static UserResponse of (Usuario user) {
        return new UserResponse(user.getId(), user.getUsername(), null, null);
    }

    public static UserResponse of (Usuario user, String token, String refreshToken) {
        return new UserResponse(user.getId(), user.getUsername(), token, refreshToken);
    }
}
