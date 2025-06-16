
package com.trianasalesianos.dam.ArmarioVirtual.service;

import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.CreateConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.GetConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.UpdateConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.error.ConjuntoNoEncontradaException;
import com.trianasalesianos.dam.ArmarioVirtual.error.PrendaNoEncontradaException;
import com.trianasalesianos.dam.ArmarioVirtual.error.TipoUsuarioInvalidoException;
import com.trianasalesianos.dam.ArmarioVirtual.model.*;
import com.trianasalesianos.dam.ArmarioVirtual.repository.ConjuntoRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.PrendaRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.TagRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConjuntoService {

    private final ConjuntoRepository conjuntoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrendaRepository prendaRepository;
    private final TagRepository tagRepository;

    private final Path conjuntoImageStorage =
            Paths.get("uploads/conjuntos").toAbsolutePath().normalize();

    @PostConstruct
    public void initImagesDir() {
        try {
            Files.createDirectories(conjuntoImageStorage);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de imágenes de conjuntos", e);
        }
    }

    @Transactional
    public GetConjuntoDto crearConjunto(CreateConjuntoDto dto) {
        Cliente cliente = usuarioRepository.findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName()
                )
                .filter(u -> u instanceof Cliente)
                .map(Cliente.class::cast)
                .orElseThrow(() -> new TipoUsuarioInvalidoException("Usuario no válido"));

        List<Prenda> prendas = dto.prendasIds().stream()
                .map(id -> prendaRepository.findById(id)
                        .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + id + " no encontrada")))
                .toList();

        Conjunto c = dto.toConjunto(prendas, cliente);

        // ** Añadir tags al conjunto **
        if (dto.tagIds() != null) {
            dto.tagIds().forEach(tagId -> {
                Tag t = tagRepository.findById(tagId)
                        .orElseThrow(() -> new RuntimeException("Tag con id " + tagId + " no encontrado"));
                c.getTags().add(t);
                t.getConjuntos().add(c);
            });
        }

        conjuntoRepository.save(c);
        return GetConjuntoDto.from(c);
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
        Conjunto c = conjuntoRepository.findById(id)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + id + " no encontrado"));

        if (c.getImagen() != null && !c.getImagen().isBlank()) {
            try {
                Path filePath = conjuntoImageStorage.resolve(c.getImagen()).normalize();
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error borrando la imagen del conjunto " + id, e);
            }
        }

        c.getTags().clear();
        c.getClientesQueDieronLike().clear();

        conjuntoRepository.delete(c);
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

    @Transactional
    public void storeConjuntoImage(Long conjuntoId, MultipartFile file) {
        Conjunto c = conjuntoRepository.findById(conjuntoId)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + conjuntoId + " no encontrado"));

        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String filename = conjuntoId + "_" + original;

        try (InputStream in = file.getInputStream()) {
            Path target = conjuntoImageStorage.resolve(filename);
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            c.setImagen(filename);
            conjuntoRepository.save(c);
        } catch (IOException ex) {
            throw new RuntimeException("Error al almacenar la imagen de conjunto " + conjuntoId, ex);
        }
    }

    public Resource loadConjuntoImage(Long conjuntoId) {
        Conjunto c = conjuntoRepository.findById(conjuntoId)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + conjuntoId + " no encontrado"));

        String filename = c.getImagen();
        if (filename == null || filename.isBlank())
            throw new RuntimeException("Este conjunto no tiene imagen asignada");

        try {
            Path filePath = conjuntoImageStorage.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer la imagen: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL inválida para la imagen: " + filename, e);
        }
    }

    public List<GetConjuntoDto> findByUsername(String username) {
        return conjuntoRepository.findByClienteUsername(username)
                .stream()
                .map(GetConjuntoDto::from)
                .toList();
    }

}
