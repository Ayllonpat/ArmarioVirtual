package com.trianasalesianos.dam.ArmarioVirtual.controller;

import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.CreateConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.GetConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.usuario.UserResponse;
import com.trianasalesianos.dam.ArmarioVirtual.service.ConjuntoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conjuntos")
@RequiredArgsConstructor
public class ConjuntoController {

    private final ConjuntoService conjuntoService;

    @Operation(summary = "Crea un conjunto seleccionando prendas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Conjunto creado exitosamente",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetConjuntoDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "id": 52,
                                                                "nombre": "Outfit Deportivo",
                                                                "imagen": "https://example.com/outfit.jpg",
                                                                "fechaPublicacion": "2025-03-01T15:20:07.2344238",
                                                                "prendas": [
                                                                    {
                                                                        "nombre": "Camiseta Negra Oversize",
                                                                        "imagen": "https://ejemplo.com/imagen.jpg",
                                                                        "color": "Negro",
                                                                        "talla": "L",
                                                                        "enlaceCompra": "https://tienda.com/camiseta-negra",
                                                                        "visibilidad": "PUBLICO",
                                                                        "tipoPrendaId": "1",
                                                                        "cliente": {
                                                                            "id": "d8fe3d58-f330-4446-8eaa-81a9796e3eed",
                                                                            "username": "patty"
                                                                        }
                                                                    }
                                                                ],
                                                                "visibilidad": "PUBLICO",
                                                                "cliente": {
                                                                    "id": "d8fe3d58-f330-4446-8eaa-81a9796e3eed",
                                                                    "username": "patty"
                                                                }
                                                            }
                                                            """
                                            )
                                    }
                            )
                    }),

            @ApiResponse(
                    responseCode = "400",
                    description = "Prenda no encontrada",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "type": "about:blank",
                                                                "title": "Prenda no encontrada",
                                                                "status": 400,
                                                                "detail": "Prenda con id 100 no encontrada",
                                                                "instance": "/api/conjuntos/a%C3%B1adir"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    }
            )
    })
    @PostMapping("/añadir")
    public ResponseEntity<GetConjuntoDto> crearConjunto(@Valid @RequestBody CreateConjuntoDto createConjuntoDto) {
        GetConjuntoDto nuevoConjunto = conjuntoService.crearConjunto(createConjuntoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoConjunto);
    }
}
