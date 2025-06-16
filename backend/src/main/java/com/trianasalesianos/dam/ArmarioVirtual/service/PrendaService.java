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
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrendaService {

    private final PrendaRepository prendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoPrendaRepository tipoPrendaRepository;

    private final Path imageStorageLocation = Paths.get("uploads/prendas").toAbsolutePath().normalize();

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(imageStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de subida de imágenes", e);
        }
    }

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
        return prendaRepository.findAllWithAll()
                .stream()
                .map(GetPrendaDto::from)
                .toList();
    }

    public GetPrendaDto findById(Long id) {
        Prenda prenda = prendaRepository.findByIdWithAll(id)
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
        Prenda prenda = prendaRepository.findById(id)
                .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + id + " no encontrada"));

        if (prenda.getImagen() != null && !prenda.getImagen().isBlank()) {
            try {
                Path filePath = imageStorageLocation.resolve(prenda.getImagen()).normalize();
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error borrando la imagen de la prenda " + id, e);
            }
        }

        prendaRepository.delete(prenda);
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

    @Transactional
    public void storeImage(Long prendaId, MultipartFile file) {
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + prendaId + " no encontrada"));

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String filename = prendaId + "_" + originalFilename;

        try (InputStream inputStream = file.getInputStream()) {
            Path targetLocation = imageStorageLocation.resolve(filename);
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            prenda.setImagen(filename);
            prendaRepository.save(prenda);
        } catch (IOException ex) {
            throw new RuntimeException("Error al almacenar la imagen de la prenda " + prendaId, ex);
        }
    }

    public Resource loadImageAsResource(Long prendaId) {
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + prendaId + " no encontrada"));

        String filename = prenda.getImagen();
        if (filename == null || filename.isBlank()) {
            throw new RuntimeException("La prenda no tiene ninguna imagen asignada");
        }

        try {
            Path filePath = imageStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer la imagen: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL de imagen inválida: " + filename, e);
        }
    }

    public Page<GetPrendaDto> search(
            String nombre,
            String color,
            List<String> tags,
            Pageable pageable) {

        Specification<Prenda> spec = Specification.where(null);

        if (nombre != null && !nombre.isBlank()) {
            spec = spec.and((root, q, cb) ->
                    cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%")
            );
        }
        if (color != null && !color.isBlank()) {
            spec = spec.and((root, q, cb) ->
                    cb.like(cb.lower(root.get("color")), "%" + color.toLowerCase() + "%")
            );
        }
        if (tags != null && !tags.isEmpty()) {
            spec = spec.and((root, q, cb) -> {
                var join = root.join("tags", JoinType.LEFT);
                q.distinct(true);
                return join.get("nombre").in(tags);
            });
        }

        return prendaRepository.findAll(spec, pageable)
                .map(GetPrendaDto::from);
    }

    public List<GetPrendaDto> findByUsername(String username) {
        return prendaRepository.findByClienteUsername(username)
                .stream().map(GetPrendaDto::from).toList();
    }

}
