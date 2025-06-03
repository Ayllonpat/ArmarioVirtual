package com.trianasalesianos.dam.ArmarioVirtual.controller;

import com.trianasalesianos.dam.ArmarioVirtual.dto.tag.CreateTagDto;
import com.trianasalesianos.dam.ArmarioVirtual.dto.tag.GetTagDto;
import com.trianasalesianos.dam.ArmarioVirtual.service.TagService;
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
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tag", description = "Controlador para gestionar tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "Crear un nuevo tag (solo ADMIN puede)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tag creado exitosamente"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GetTagDto crearTag(@Valid @RequestBody CreateTagDto dto) {
        return tagService.crearTag(dto);
    }

    @Operation(summary = "Listar todos los tags")
    @ApiResponse(responseCode = "200", description = "Listado de tags obtenido")
    @GetMapping
    public ResponseEntity<List<GetTagDto>> listarTags() {
        return ResponseEntity.ok(tagService.listarTags());
    }

    @Operation(summary = "Eliminar un tag por ID (solo ADMIN puede)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag eliminado correctamente"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tag no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTag(@PathVariable Long id) {
        tagService.eliminarTag(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asociar un tag a una prenda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag asociado a la prenda"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prenda o tag no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/prendas/{prendaId}/tags/{tagId}")
    public ResponseEntity<Void> añadirTagAPrenda(@PathVariable Long prendaId, @PathVariable Long tagId) {
        tagService.añadirTagAPrenda(prendaId, tagId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar un tag de una prenda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag eliminado de la prenda"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prenda o tag no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/prendas/{prendaId}/tags/{tagId}")
    public ResponseEntity<Void> eliminarTagDePrenda(@PathVariable Long prendaId, @PathVariable Long tagId) {
        tagService.eliminarTagDePrenda(prendaId, tagId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Asociar un tag a un conjunto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag asociado al conjunto"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conjunto o tag no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/conjuntos/{conjuntoId}/tags/{tagId}")
    public ResponseEntity<Void> añadirTagAConjunto(@PathVariable Long conjuntoId, @PathVariable Long tagId) {
        tagService.añadirTagAConjunto(conjuntoId, tagId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar un tag de un conjunto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag eliminado del conjunto"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conjunto o tag no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/conjuntos/{conjuntoId}/tags/{tagId}")
    public ResponseEntity<Void> eliminarTagDeConjunto(@PathVariable Long conjuntoId, @PathVariable Long tagId) {
        tagService.eliminarTagDeConjunto(conjuntoId, tagId);
        return ResponseEntity.ok().build();
    }
}
