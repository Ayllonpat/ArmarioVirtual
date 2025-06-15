package com.trianasalesianos.dam.ArmarioVirtual.controller;

import com.trianasalesianos.dam.ArmarioVirtual.dto.comentario.CreateComentarioDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.comentario.GetComentarioDto;
import com.trianasalesianos.dam.ArmarioVirtual.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Comentario", description = "Controlador para gestionar comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @Operation(summary = "Crear comentario en una prenda")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comentario creado exitosamente"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación o usuario no válido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prenda no encontrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/prendas/{prendaId}/comentarios")
    @ResponseStatus(HttpStatus.CREATED)
    public GetComentarioDto crearComentarioEnPrenda(
            @PathVariable Long prendaId,
            @Valid @RequestBody CreateComentarioDto dto) {
        return comentarioService.crearComentarioEnPrenda(prendaId, dto);
    }

    @Operation(summary = "Listar comentarios de una prenda")
    @ApiResponse(responseCode = "200", description = "Lista de comentarios obtenida exitosamente")
    @ApiResponse(
            responseCode = "404",
            description = "Prenda no encontrada",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    @GetMapping("/prendas/{prendaId}/comentarios")
    public ResponseEntity<List<GetComentarioDto>> listarComentariosDePrenda(@PathVariable Long prendaId) {
        return ResponseEntity.ok(comentarioService.listarComentariosDePrenda(prendaId));
    }

    @Operation(summary = "Crear comentario en un conjunto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comentario creado exitosamente"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación o usuario no válido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conjunto no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/conjuntos/{conjuntoId}/comentarios")
    @ResponseStatus(HttpStatus.CREATED)
    public GetComentarioDto crearComentarioEnConjunto(
            @PathVariable Long conjuntoId,
            @Valid @RequestBody CreateComentarioDto dto) {
        return comentarioService.crearComentarioEnConjunto(conjuntoId, dto);
    }

    @Operation(summary = "Listar comentarios de un conjunto")
    @ApiResponse(responseCode = "200", description = "Lista de comentarios obtenida exitosamente")
    @ApiResponse(
            responseCode = "404",
            description = "Conjunto no encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    @GetMapping("/conjuntos/{conjuntoId}/comentarios")
    public ResponseEntity<List<GetComentarioDto>> listarComentariosDeConjunto(@PathVariable Long conjuntoId) {
        return ResponseEntity.ok(comentarioService.listarComentariosDeConjunto(conjuntoId));
    }

    @Operation(summary = "Eliminar un comentario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comentario eliminado exitosamente"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comentario no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }
}
