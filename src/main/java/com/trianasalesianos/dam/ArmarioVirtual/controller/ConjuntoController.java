
package com.trianasalesianos.dam.ArmarioVirtual.controller;

import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.CreateConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.GetConjuntoDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.conjunto.UpdateConjuntoDto;
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

import java.util.List;

@RestController
@RequestMapping("/api/conjuntos")
@RequiredArgsConstructor
public class ConjuntoController {

    private final ConjuntoService conjuntoService;

    @Operation(summary = "Listar todos los conjuntos")
    @ApiResponse(
            responseCode = "200",
            description = "Listado de conjuntos obtenido correctamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetConjuntoDto.class)
            )
    )
    @GetMapping
    public ResponseEntity<List<GetConjuntoDto>> listAll() {
        return ResponseEntity.ok(conjuntoService.findAll());
    }

    @Operation(summary = "Obtener detalle de un conjunto por ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Conjunto encontrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetConjuntoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conjunto no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        \"type\": \"about:blank\",
                        \"title\": \"Conjunto no encontrado\",
                        \"status\": 404,
                        \"detail\": \"Conjunto con id 100 no encontrado\",
                        \"instance\": \"/api/conjuntos/100\"
                    }
                    """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetConjuntoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(conjuntoService.findById(id));
    }

    @Operation(summary = "Crear un nuevo conjunto")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Conjunto creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetConjuntoDto.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                      \"id\": 52,
                      \"nombre\": \"Outfit Deportivo\",
                      \"imagen\": \"https://example.com/outfit.jpg\",
                      \"fechaPublicacion\": \"2025-03-01T15:20:07.2344238\",
                      \"prendas\": [ /* ... */ ],
                      \"visibilidad\": \"PUBLICO\",
                      \"cliente\": { /* ... */ }
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Prenda no encontrada o datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        \"type\": \"about:blank\",
                        \"title\": \"Prenda no encontrada\",
                        \"status\": 400,
                        \"detail\": \"Prenda con id 100 no encontrada\",
                        \"instance\": \"/api/conjuntos/a%C3%B1adir\"
                    }
                    """
                            )
                    )
            )
    })
    @PostMapping("/añadir")
    public ResponseEntity<GetConjuntoDto> crearConjunto(@Valid @RequestBody CreateConjuntoDto dto) {
        GetConjuntoDto nuevo = conjuntoService.crearConjunto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Actualizar un conjunto existente")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Conjunto actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetConjuntoDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o conjunto no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        \"type\": \"about:blank\",
                        \"title\": \"Conjunto no encontrado\",
                        \"status\": 400,
                        \"detail\": \"Conjunto con id 100 no encontrado\",
                        \"instance\": \"/api/conjuntos/100\"
                    }
                    """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<GetConjuntoDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateConjuntoDto dto) {
        return ResponseEntity.ok(conjuntoService.update(id, dto));
    }

    @Operation(summary = "Eliminar un conjunto por ID")
    @ApiResponse(
            responseCode = "204",
            description = "Conjunto eliminado exitosamente"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        conjuntoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Dar o quitar un like a un conjunto")
    @ApiResponse(responseCode = "200", description = "Like dado o quitado exitosamente")
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long id) {
        conjuntoService.toggleLike(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener el conteo de likes de un conjunto")
    @ApiResponse(responseCode = "200", description = "Número de likes obtenido exitosamente", content = @Content(schema = @Schema(implementation = Long.class)))
    @GetMapping("/{id}/likes")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long id) {
        return ResponseEntity.ok(conjuntoService.getLikeCount(id));
    }
}