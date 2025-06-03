package com.trianasalesianos.dam.ArmarioVirtual.service;

import com.trianasalesianos.dam.ArmarioVirtual.dto.tag.CreateTagDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.tag.GetTagDto;
import com.trianasalesianos.dam.ArmarioVirtual.error.TagNoEncontradoException;
import com.trianasalesianos.dam.ArmarioVirtual.error.PrendaNoEncontradaException;
import com.trianasalesianos.dam.ArmarioVirtual.error.ConjuntoNoEncontradaException;
import com.trianasalesianos.dam.ArmarioVirtual.model.Conjunto;
import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import com.trianasalesianos.dam.ArmarioVirtual.model.Tag;
import com.trianasalesianos.dam.ArmarioVirtual.repository.ConjuntoRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.PrendaRepository;
import com.trianasalesianos.dam.ArmarioVirtual.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final PrendaRepository prendaRepository;
    private final ConjuntoRepository conjuntoRepository;

    @Transactional
    public GetTagDto crearTag(CreateTagDto dto) {
        Tag tag = Tag.builder()
                .nombre(dto.nombre())
                .build();
        tagRepository.save(tag);
        return GetTagDto.from(tag);
    }

    public List<GetTagDto> listarTags() {
        return tagRepository.findAll()
                .stream()
                .map(GetTagDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNoEncontradoException("Tag con id " + id + " no encontrado"));
        tagRepository.delete(tag);
    }

    @Transactional
    public void añadirTagAPrenda(Long prendaId, Long tagId) {
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + prendaId + " no encontrada"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNoEncontradoException("Tag con id " + tagId + " no encontrado"));
        prenda.getTags().add(tag);
        tag.getPrendas().add(prenda);
        prendaRepository.save(prenda);
    }

    @Transactional
    public void eliminarTagDePrenda(Long prendaId, Long tagId) {
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new PrendaNoEncontradaException("Prenda con id " + prendaId + " no encontrada"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNoEncontradoException("Tag con id " + tagId + " no encontrado"));
        prenda.getTags().remove(tag);
        tag.getPrendas().remove(prenda);
        prendaRepository.save(prenda);
    }

    @Transactional
    public void añadirTagAConjunto(Long conjuntoId, Long tagId) {
        Conjunto conjunto = conjuntoRepository.findById(conjuntoId)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + conjuntoId + " no encontrado"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNoEncontradoException("Tag con id " + tagId + " no encontrado"));
        conjunto.getTags().add(tag);
        tag.getConjuntos().add(conjunto);
        conjuntoRepository.save(conjunto);
    }

    @Transactional
    public void eliminarTagDeConjunto(Long conjuntoId, Long tagId) {
        Conjunto conjunto = conjuntoRepository.findById(conjuntoId)
                .orElseThrow(() -> new ConjuntoNoEncontradaException("Conjunto con id " + conjuntoId + " no encontrado"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNoEncontradoException("Tag con id " + tagId + " no encontrado"));
        conjunto.getTags().remove(tag);
        tag.getConjuntos().remove(conjunto);
        conjuntoRepository.save(conjunto);
    }
}
