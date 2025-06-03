package com.trianasalesianos.dam.ArmarioVirtual.service;

import com.trianasalesianos.dam.ArmarioVirtual.dto.comentario.CreateComentarioDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.comentario.GetComentarioDto;
import com.trianasalesianos.dam.ArmarioVirtual.error.ComentarioNoEncontradoException;
import com.trianasalesianos.dam.ArmarioVirtual.error.PrendaNoEncontradaException;
import com.trianasalesianos.dam.ArmarioVirtual.error.ConjuntoNoEncontradaException;
import com.trianasalesianos.dam.ArmarioVirtual.error.TipoUsuarioInvalidoException;
import com.trianasalesianos.dam.ArmarioVirtual.model.Comentario;
import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import com.trianasalesianos.dam.ArmarioVirtual.model.Conjunto;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.repository.ComentarioRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.PrendaRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.ConjuntoRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrendaRepository prendaRepository;
    private final ConjuntoRepository conjuntoRepository;

    @Transactional
    public GetComentarioDto crearComentarioEnPrenda(Long prendaId, CreateComentarioDto dto) {
        Usuario usuario = usuarioRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new TipoUsuarioInvalidoException("Usuario no encontrado"));
        if (!(usuario instanceof Cliente cliente)) {
            throw new TipoUsuarioInvalidoException("Solo clientes pueden comentar");
        }
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + prendaId + " no encontrada"));

        Comentario comentario = Comentario.builder()
                .contenido(dto.contenido())
                .fechaPublicacion(LocalDateTime.now())
                .cliente(cliente)
                .prenda(prenda)
                .build();

        comentarioRepository.save(comentario);
        return GetComentarioDto.from(comentario);
    }

    @Transactional
    public GetComentarioDto crearComentarioEnConjunto(Long conjuntoId, CreateComentarioDto dto) {
        Usuario usuario = usuarioRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new TipoUsuarioInvalidoException("Usuario no encontrado"));
        if (!(usuario instanceof Cliente cliente)) {
            throw new TipoUsuarioInvalidoException("Solo clientes pueden comentar");
        }
        Conjunto conjunto = conjuntoRepository.findById(conjuntoId)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + conjuntoId + " no encontrado"));

        Comentario comentario = Comentario.builder()
                .contenido(dto.contenido())
                .fechaPublicacion(LocalDateTime.now())
                .cliente(cliente)
                .conjunto(conjunto)
                .build();

        comentarioRepository.save(comentario);
        return GetComentarioDto.from(comentario);
    }

    public List<GetComentarioDto> listarComentariosDePrenda(Long prendaId) {
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + prendaId + " no encontrada"));
        return comentarioRepository.findAll()
                .stream()
                .filter(c -> prenda.equals(c.getPrenda()))
                .map(GetComentarioDto::from)
                .collect(Collectors.toList());
    }

    public List<GetComentarioDto> listarComentariosDeConjunto(Long conjuntoId) {
        Conjunto conjunto = conjuntoRepository.findById(conjuntoId)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + conjuntoId + " no encontrado"));
        return comentarioRepository.findAll()
                .stream()
                .filter(c -> conjunto.equals(c.getConjunto()))
                .map(GetComentarioDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarComentario(Long comentarioId) {
        if (!comentarioRepository.existsById(comentarioId)) {
            throw new ComentarioNoEncontradoException("Comentario con id " + comentarioId + " no encontrado");
        }
        comentarioRepository.deleteById(comentarioId);
    }
}
