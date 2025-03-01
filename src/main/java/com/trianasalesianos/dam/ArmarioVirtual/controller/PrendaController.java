package com.trianasalesianos.dam.ArmarioVirtual.controller;

import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.GetConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.CreatePrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.GetPrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.LikeCountDto;
import com.trianasalesianos.dam.ArmarioVirtual.model.Prenda;
import com.trianasalesianos.dam.ArmarioVirtual.service.PrendaService;
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
@RequestMapping("/api/prendas")
@RequiredArgsConstructor
public class PrendaController {

    private final PrendaService prendaService;

    @Operation(summary = "Crear una nueva prenda")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Prenda creada exitosamente",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetPrendaDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                 "nombre": "Camiseta",
                                                                 "imagen": "https://example.com/imagen.jpg",
                                                                 "color": "Rojo",
                                                                 "talla": "M",
                                                                 "enlaceCompra": "https://tienda.com/camiseta",
                                                                 "visibilidad": "PRIVADO",
                                                                 "tipoPrendaId": "1",
                                                                 "cliente": {
                                                                     "id": "d8fe3d58-f330-4446-8eaa-81a9796e3eed",
                                                                     "username": "patty"
                                                                 }
                                                             }
                                                            """
                                            )
                                    }
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "TipoPrenda no encontrado",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "type": "about:blank",
                                                                "title": "TipoPrenda no encontrada",
                                                                "status": 400,
                                                                "detail": "Tipo de prenda no encontrado",
                                                                "instance": "/api/prendas/a%C3%B1adir"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    }
            )
    })
    @PostMapping("/añadir")
    public ResponseEntity<GetPrendaDto> crearPrenda(@Valid @RequestBody CreatePrendaDto createPrendaDto) {
        GetPrendaDto prendaDto = prendaService.crearPrenda(createPrendaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(prendaDto);
    }

    @Operation(summary = "Dar o quitar like a una prenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like actualizado correctamente"),
            @ApiResponse(responseCode = "400",
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
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long id) {
        prendaService.toggleLike(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener el número de likes de una prenda")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Número de likes obtenido exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prenda no encontrada"
            )
    })
    @GetMapping("/{id}/likes")
    public ResponseEntity<LikeCountDto> getLikeCount(@PathVariable Long id) {
        LikeCountDto likeCountDto = prendaService.getLikeCount(id);
        return ResponseEntity.ok(likeCountDto);
    }
}
