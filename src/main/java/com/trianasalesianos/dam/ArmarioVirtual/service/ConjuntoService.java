package com.trianasalesianos.dam.ArmarioVirtual.service;

import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.CreateConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.GetConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.error.ConjuntoNoEncontradaException;
import com.trianasalesianos.dam.ArmarioVirtual.error.PrendaNoEncontradaException;
import com.trianasalesianos.dam.ArmarioVirtual.error.TipoUsuarioInvalidoException;
import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.model.Conjunto;
import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import com.trianasalesianos.dam.ArmarioVirtual.repository.ConjuntoRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.PrendaRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConjuntoService {

    private final ConjuntoRepository conjuntoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrendaRepository prendaRepository;

    @Transactional
    public GetConjuntoDto crearConjunto(CreateConjuntoDto createConjuntoDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new TipoUsuarioInvalidoException("Usuario no encontrado"));

        if (!(usuario instanceof Cliente cliente)) {
            throw new TipoUsuarioInvalidoException("El usuario no es un cliente válido");
        }

        List<Prenda> prendas = createConjuntoDto.prendasIds().stream()
                .map(id -> prendaRepository.findById(id)
                        .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + id + " no encontrada")))
                .toList();

        Conjunto conjunto = createConjuntoDto.toConjunto(prendas, cliente);

        conjuntoRepository.save(conjunto);

        return GetConjuntoDto.from(conjunto);
    }

    @Transactional
    public void toggleLike(Long conjuntoId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Cliente cliente = usuarioRepository.findByUsername(username)
                .filter(usuario -> usuario instanceof Cliente)
                .map(Cliente.class::cast)
                .orElseThrow(() -> new TipoUsuarioInvalidoException("Usuario no válido"));

        Conjunto conjunto = conjuntoRepository.findById(conjuntoId)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto no encontrado"));

        if (conjunto.getClientesQueDieronLike().contains(cliente)) {
            conjunto.getClientesQueDieronLike().remove(cliente);
        } else {
            conjunto.getClientesQueDieronLike().add(cliente);
        }

        conjuntoRepository.save(conjunto);
    }

    public long getLikeCount(Long conjuntoId) {
        Conjunto conjunto = conjuntoRepository.findById(conjuntoId)
                .orElseThrow(() -> new PrendaNoEncontradaException("Conjunto no encontrado"));

        return conjunto.getClientesQueDieronLike().size();
    }

}
