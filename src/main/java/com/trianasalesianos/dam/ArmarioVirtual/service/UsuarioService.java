// src/main/java/com/trianasalesianos/dam/ArmarioVirtual/service/UsuarioService.java
package com.trianasalesianos.dam.ArmarioVirtual.service;

import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.CreateUsuarioDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.GetClienteDto;
import com.trianasalesianos.dam.ArmarioVirtual.error.EmailDuplicadoException;
import com.trianasalesianos.dam.ArmarioVirtual.error.TipoUsuarioInvalidoException;
import com.trianasalesianos.dam.ArmarioVirtual.error.UsernameDuplicadoException;
import com.trianasalesianos.dam.ArmarioVirtual.error.UsuarioNoAutenticadoException;
import com.trianasalesianos.dam.ArmarioVirtual.error.UsuarioNoEncontradoException;
import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.ActivationToken;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.service.ActivationTokenService;
import com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.service.EmailService;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UsuarioNoAutenticadoException("No autenticado. Debes iniciar sesión.");
        }
        return (Usuario) authentication.getPrincipal();
    }

    @Transactional
    public void follow(UUID targetId) {
        Usuario actor = obtenerUsuarioAutenticado();
        if (!(actor instanceof Cliente cliente)) {
            throw new TipoUsuarioInvalidoException("Solo clientes pueden seguir a otros usuarios");
        }
        Usuario target = usuarioRepository.findById(targetId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con id " + targetId + " no encontrado"));
        if (!(target instanceof Cliente clienteTarget)) {
            throw new TipoUsuarioInvalidoException("No puedes seguir a este tipo de usuario");
        }
        cliente.getSeguidos().add(clienteTarget);
        clienteTarget.getSeguidores().add(cliente);
        usuarioRepository.save(cliente);
        usuarioRepository.save(clienteTarget);
    }

    @Transactional
    public void unfollow(UUID targetId) {
        Usuario actor = obtenerUsuarioAutenticado();
        if (!(actor instanceof Cliente cliente)) {
            throw new TipoUsuarioInvalidoException("Solo clientes pueden dejar de seguir a otros usuarios");
        }
        Usuario target = usuarioRepository.findById(targetId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con id " + targetId + " no encontrado"));
        if (!(target instanceof Cliente clienteTarget)) {
            throw new TipoUsuarioInvalidoException("No puedes dejar de seguir a este tipo de usuario");
        }
        cliente.getSeguidos().remove(clienteTarget);
        clienteTarget.getSeguidores().remove(cliente);
        usuarioRepository.save(cliente);
        usuarioRepository.save(clienteTarget);
    }

    @Transactional(readOnly = true)
    public List<GetClienteDto> getSeguidores(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con id " + id + " no encontrado"));
        if (!(usuario instanceof Cliente cliente)) {
            throw new TipoUsuarioInvalidoException("El usuario no es un cliente válido");
        }
        return cliente.getSeguidores()
                .stream()
                .map(GetClienteDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GetClienteDto> getSeguidos(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con id " + id + " no encontrado"));
        if (!(usuario instanceof Cliente cliente)) {
            throw new TipoUsuarioInvalidoException("El usuario no es un cliente válido");
        }
        return cliente.getSeguidos()
                .stream()
                .map(GetClienteDto::from)
                .collect(Collectors.toList());
    }

}
