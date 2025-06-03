
package com.trianasalesianos.dam.ArmarioVirtual.controller;

import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.CreatePrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.GetPrendaDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.LikeCountDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.prenda.UpdatePrendaDto;
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

import java.util.List;

@RestController
@RequestMapping("/api/prendas")
@RequiredArgsConstructor
public class PrendaController {

    private final PrendaService prendaService;

    @Operation(summary = "Listar todas las prendas")
    @ApiResponse(
            responseCode = "200",
            description = "Listado de prendas obtenido correctamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetPrendaDto.class)
            )
    )
    @GetMapping
    public ResponseEntity<List<GetPrendaDto>> listAll() {
        return ResponseEntity.ok(prendaService.findAll());
    }

    @Operation(summary = "Obtener detalle de una prenda por ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Prenda encontrada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetPrendaDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prenda no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "type": "about:blank",
                        "title": "Prenda no encontrada",
                        "status": 404,
                        "detail": "Prenda con id 100 no encontrada",
                        "instance": "/api/prendas/100"
                    }
                    """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetPrendaDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(prendaService.findById(id));
    }

    @Operation(summary = "Crear una nueva prenda")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Prenda creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetPrendaDto.class),
                            examples = @ExampleObject(
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
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "TipoPrenda no encontrado o datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
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
                    )
            )
    })
    @PostMapping("/añadir")
    public ResponseEntity<GetPrendaDto> crearPrenda(@Valid @RequestBody CreatePrendaDto createPrendaDto) {
        GetPrendaDto prendaDto = prendaService.crearPrenda(createPrendaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(prendaDto);
    }

    @Operation(summary = "Actualizar una prenda existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Prenda actualizada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetPrendaDto.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                         "nombre": "Camiseta Actualizada",
                         "imagen": "https://example.com/imagen2.jpg",
                         "color": "Azul",
                         "talla": "L",
                         "enlaceCompra": "https://tienda.com/camiseta2",
                         "visibilidad": "PUBLICO",
                         "tipoPrendaId": "2",
                         "cliente": {
                             "id": "d8fe3d58-f330-4446-8eaa-81a9796e3eed",
                             "username": "patty"
                         }
                     }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o prenda no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "type": "about:blank",
                        "title": "Prenda no encontrada",
                        "status": 400,
                        "detail": "Prenda con id 100 no encontrada",
                        "instance": "/api/prendas/100"
                    }
                    """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<GetPrendaDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePrendaDto updatePrendaDto) {
        return ResponseEntity.ok(prendaService.update(id, updatePrendaDto));
    }

    @Operation(summary = "Eliminar una prenda por ID")
    @ApiResponse(
            responseCode = "204",
            description = "Prenda eliminada exitosamente"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prendaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Dar o quitar like a una prenda")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Like actualizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Prenda no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "type": "about:blank",
                        "title": "Prenda no encontrada",
                        "status": 400,
                        "detail": "Prenda con id 100 no encontrada",
                        "instance": "/api/prendas/100/like"
                    }
                    """
                            )
                    )
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
                    description = "Número de likes obtenido exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LikeCountDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prenda no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "type": "about:blank",
                        "title": "Prenda no encontrada",
                        "status": 404,
                        "detail": "Prenda con id 100 no encontrada",
                        "instance": "/api/prendas/100/likes"
                    }
                    """
                            )
                    )
            )
    })
    @GetMapping("/{id}/likes")
    public ResponseEntity<LikeCountDto> getLikeCount(@PathVariable Long id) {
        LikeCountDto likeCountDto = prendaService.getLikeCount(id);
        return ResponseEntity.ok(likeCountDto);
    }
}
