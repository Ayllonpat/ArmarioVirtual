package com.trianasalesianos.dam.ArmarioVirtual.dto.usuario;

import com.trianasalesianos.dam.ArmarioVirtual.model.Admin;
import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;

public record CreateUsuarioDto(
        @NotBlank(message = "{error.nombre.blank}")
        @Size(max = 150, message = "{error.nombre.size}")
        String nombre,

        @NotBlank(message = "{error.username.blank}")
        @Size(max = 150, message = "{error.username.size}")
        String username,

        @NotBlank(message = "{error.password.blank}")
        @Size(min = 6, message = "{error.password.size}")
        String password,

        @NotBlank(message = "{error.email.blank}")
        @Email(message = "{error.email.invalid}")
        String email
) {
    public Cliente toCliente(PasswordEncoder passwordEncoder) {
        return Cliente.builder()
                .nombre(nombre)
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .enable(false)
                .role(Role.CLIENTE)
                .activo(true)
                .build();
    }

    public Admin toAdmin(PasswordEncoder passwordEncoder) {
        return Admin.builder()
                .nombre(nombre)
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(Role.ADMIN)
                .enable(false)
                .activo(true)
                .build();
    }
}
