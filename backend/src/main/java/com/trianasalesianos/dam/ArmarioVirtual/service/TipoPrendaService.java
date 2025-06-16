package com.trianasalesianos.dam.ArmarioVirtual.service;

import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.GetTipoPrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.model.TipoPrenda;
import com.trianasalesianos.dam.ArmarioVirtual.repository.TipoPrendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoPrendaService {

    private final TipoPrendaRepository tipoPrendaRepository;

    public List<GetTipoPrendaDto> findAll() {
        return tipoPrendaRepository.findAll()
                .stream()
                .map(GetTipoPrendaDto::from)
                .toList();
    }
}