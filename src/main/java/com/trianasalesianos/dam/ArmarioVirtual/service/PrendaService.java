// src/main/java/com/trianasalesianos/dam/ArmarioVirtual/service/PrendaService.java
package com.trianasalesianos.dam.ArmarioVirtual.service;

import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.CreatePrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.GetPrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.LikeCountDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.UpdatePrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.error.PrendaNoEncontradaException;
import com.trianasalesianos.dam.ArmarioVirtual.error.TipoPrendaNoEncontradaException;
import com.trianasalesianos.dam.ArmarioVirtual.error.TipoUsuarioInvalidoException;
import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import com.trianasalesianos.dam.ArmarioVirtual.model.TipoPrenda;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import com.trianasalesianos.dam.ArmarioVirtual.repository.PrendaRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.TipoPrendaRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrendaService {

    private final PrendaRepository prendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoPrendaRepository tipoPrendaRepository;

    @Transactional
    public GetPrendaDto crearPrenda(CreatePrendaDto createPrendaDto) {
        Usuario usuario = usuarioRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new TipoUsuarioInvalidoException("Usuario no encontrado"));

        if (!(usuario instanceof Cliente cliente)) {
            throw new TipoUsuarioInvalidoException("El usuario no es un cliente válido");
        }

        TipoPrenda tipoPrenda = tipoPrendaRepository.findById(createPrendaDto.tipoPrenda().id())
                .orElseThrow(() -> new TipoPrendaNoEncontradaException("Tipo de prenda no encontrado"));

        Prenda prenda = createPrendaDto.toPrenda(cliente, tipoPrenda);
        prendaRepository.save(prenda);
        return GetPrendaDto.from(prenda);
    }

    public List<GetPrendaDto> findAll() {
        return prendaRepository.findAll()
                .stream()
                .map(GetPrendaDto::from)
                .toList();
    }

    public GetPrendaDto findById(Long id) {
        Prenda prenda = prendaRepository.findById(id)
                .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + id + " no encontrada"));
        return GetPrendaDto.from(prenda);
    }

    @Transactional
    public GetPrendaDto update(Long id, UpdatePrendaDto dto) {
        Prenda prenda = prendaRepository.findById(id)
                .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + id + " no encontrada"));

        prenda.setNombre(dto.nombre());
        prenda.setImagen(dto.imagen());
        prenda.setColor(dto.color());
        prenda.setTalla(dto.talla());
        prenda.setEnlaceCompra(dto.enlaceCompra());
        prenda.setVisibilidad(dto.visibilidad() != null ? dto.visibilidad() : prenda.getVisibilidad());

        if (dto.tipoPrendaId() != null) {
            TipoPrenda tipo = tipoPrendaRepository.findById(dto.tipoPrendaId())
                    .orElseThrow(() -> new TipoPrendaNoEncontradaException("Tipo de prenda no encontrado"));
            prenda.setTipoPrenda(tipo);
        }

        prendaRepository.save(prenda);
        return GetPrendaDto.from(prenda);
    }

    @Transactional
    public void delete(Long id) {
        if (!prendaRepository.existsById(id)) {
            throw new PrendaNoEncontradaException("Prenda con id " + id + " no encontrada");
        }
        prendaRepository.deleteById(id);
    }

    @Transactional
    public void toggleLike(Long prendaId) {
        Cliente cliente = usuarioRepository.findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName()
                ).filter(usuario -> usuario instanceof Cliente)
                .map(Cliente.class::cast)
                .orElseThrow(() -> new TipoUsuarioInvalidoException("Usuario no válido"));

        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + prendaId + " no encontrada"));

        if (prendaRepository.existsLikeByCliente(prendaId, cliente.getId())) {
            prenda.getClientesQueDieronLike().remove(cliente);
        } else {
            prenda.getClientesQueDieronLike().add(cliente);
        }

        prendaRepository.save(prenda);
    }

    public LikeCountDto getLikeCount(Long prendaId) {
        if (!prendaRepository.existsById(prendaId)) {
            throw new PrendaNoEncontradaException("Prenda con id " + prendaId + " no encontrada");
        }
        long likeCount = prendaRepository.countLikes(prendaId);
        return new LikeCountDto(likeCount);
    }
}
