package com.trianasalesianos.dam.ArmarioVirtual.service;

import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.CreateUsuarioDto;
import com.trianasalesianos.dam.ArmarioVirtual.error.EmailDuplicadoException;
import com.trianasalesianos.dam.ArmarioVirtual.error.TipoUsuarioInvalidoException;
import com.trianasalesianos.dam.ArmarioVirtual.error.UsernameDuplicadoException;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario crearUsuario(CreateUsuarioDto createUsuarioDto, String tipoUsuario) {

        if (usuarioRepository.findByEmail(createUsuarioDto.email()).isPresent()) {
            throw new EmailDuplicadoException("El correo electrónico ya está registrado.");
        }

        if (usuarioRepository.findByUsername(createUsuarioDto.username()).isPresent()) {
            throw new UsernameDuplicadoException("El nombre de usuario ya está en uso.");
        }

        Usuario usuario;
        switch (tipoUsuario.toLowerCase()) {
            case "cliente":
                usuario = usuarioRepository.save(createUsuarioDto.toCliente(passwordEncoder));
                break;
            case "admin":
                usuario = usuarioRepository.save(createUsuarioDto.toAdmin(passwordEncoder));
                break;
            default:
                throw new TipoUsuarioInvalidoException("El tipo de usuario es inválido.");
        }

        return usuario;
    }
}
