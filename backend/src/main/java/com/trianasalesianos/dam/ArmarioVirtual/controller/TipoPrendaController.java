package com.trianasalesianos.dam.ArmarioVirtual.controller;

import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.GetTipoPrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.service.TipoPrendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-prenda")
@RequiredArgsConstructor
public class TipoPrendaController {

    private final TipoPrendaService tipoPrendaService;

    @GetMapping
    public ResponseEntity<List<GetTipoPrendaDto>> listAll() {
        List<GetTipoPrendaDto> tipos = tipoPrendaService.findAll();
        return ResponseEntity.ok(tipos);
    }
}