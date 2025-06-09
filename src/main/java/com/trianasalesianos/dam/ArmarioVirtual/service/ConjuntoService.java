
package com.trianasalesianos.dam.ArmarioVirtual.service;

import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.CreateConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.GetConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.UpdateConjuntoDto;
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
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


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
        Usuario usuario = usuarioRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new TipoUsuarioInvalidoException("Usuario no encontrado"));

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

    public List<GetConjuntoDto> findAll() {
        return conjuntoRepository.findAllWithAll()
                .stream()
                .map(GetConjuntoDto::from)
                .toList();
    }

    public GetConjuntoDto findById(Long id) {
        Conjunto conjunto = conjuntoRepository.findByIdWithAll(id)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + id + " no encontrado"));
        return GetConjuntoDto.from(conjunto);
    }

    @Transactional
    public GetConjuntoDto update(Long id, UpdateConjuntoDto dto) {
        Conjunto conjunto = conjuntoRepository.findById(id)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + id + " no encontrado"));

        conjunto.setNombre(dto.nombre());
        conjunto.setImagen(dto.imagen());
        conjunto.setVisibilidad(dto.visibilidad() != null ? dto.visibilidad() : conjunto.getVisibilidad());

        List<Prenda> prendas = dto.prendasIds().stream()
                .map(pid -> prendaRepository.findById(pid)
                        .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + pid + " no encontrada")))
                .toList();
        conjunto.setPrendas(prendas);

        conjuntoRepository.save(conjunto);
        return GetConjuntoDto.from(conjunto);
    }

    @Transactional
    public void delete(Long id) {
        if (!conjuntoRepository.existsById(id)) {
            throw new ConjuntoNoEncontradaException("Conjunto con id " + id + " no encontrado");
        }
        conjuntoRepository.deleteById(id);
    }

    @Transactional
    public void toggleLike(Long conjuntoId) {
        Cliente cliente = usuarioRepository.findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName()
                ).filter(usuario -> usuario instanceof Cliente)
                .map(Cliente.class::cast)
                .orElseThrow(() -> new TipoUsuarioInvalidoException("Usuario no válido"));

        Conjunto conjunto = conjuntoRepository.findById(conjuntoId)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + conjuntoId + " no encontrado"));

        if (conjunto.getClientesQueDieronLike().contains(cliente)) {
            conjunto.getClientesQueDieronLike().remove(cliente);
        } else {
            conjunto.getClientesQueDieronLike().add(cliente);
        }
        conjuntoRepository.save(conjunto);
    }

    public long getLikeCount(Long conjuntoId) {
        Conjunto conjunto = conjuntoRepository.findById(conjuntoId)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + conjuntoId + " no encontrado"));
        return conjunto.getClientesQueDieronLike().size();
    }

    public Page<GetConjuntoDto> search(
            String nombre,
            List<String> tags,
            Pageable pageable) {

        Specification<Conjunto> spec = Specification.where(null);

        if (nombre != null && !nombre.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%")
            );
        }
        if (tags != null && !tags.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                var prendasJoin = root.join("prendas", JoinType.LEFT);
                var tagJoin = prendasJoin.join("tags", JoinType.LEFT);
                query.distinct(true);
                return tagJoin.get("nombre").in(tags);
            });
        }

        return conjuntoRepository.findAll(spec, pageable)
                .map(GetConjuntoDto::from);
    }
}
