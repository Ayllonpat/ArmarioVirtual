package com.trianasalesianos.dam.ArmarioVirtual.service;

import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.CreateUsuarioDto;
import com.trianasalesianos.dam.ArmarioVirtual.error.EmailDuplicadoException;
import com.trianasalesianos.dam.ArmarioVirtual.error.TipoUsuarioInvalidoException;
import com.trianasalesianos.dam.ArmarioVirtual.error.UsernameDuplicadoException;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.ActivationToken;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.service.ActivationTokenService;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivationTokenService activationTokenService;
    private final EmailService emailService;

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
                usuario = createUsuarioDto.toCliente(passwordEncoder);
                break;
            case "admin":
                usuario = createUsuarioDto.toAdmin(passwordEncoder);
                break;
            default:
                throw new TipoUsuarioInvalidoException("El tipo de usuario es inválido.");
        }

        usuario.setEnable(false);
        usuario = usuarioRepository.save(usuario);

        ActivationToken token = activationTokenService.createToken(usuario);
        emailService.sendActivationEmail(usuario.getEmail(), token.getToken());

        return usuario;
    }
}
