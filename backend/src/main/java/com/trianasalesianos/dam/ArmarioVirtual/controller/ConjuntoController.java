
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.stream.Collectors;

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

    @Operation(summary = "Obtener todas las URLs de las imágenes de las prendas que componen un conjunto")
    @GetMapping("/{id}/imagenes")
    public ResponseEntity<List<String>> listarImagenes(@PathVariable Long id) {
        GetConjuntoDto conjunto = conjuntoService.findById(id);
        List<String> urls = conjunto.prendas()
                .stream()
                .map(p -> p.imagenUrl())
                .collect(Collectors.toList());
        return ResponseEntity.ok(urls);
    }

    @Operation(summary = "Listar conjuntos (paginado y filtrable por nombre y tags)")
    @GetMapping("/pag")
    public ResponseEntity<Page<GetConjuntoDto>> listAllPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) List<String> tags
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetConjuntoDto> resultados = conjuntoService.search(nombre, tags, pageable);
        return ResponseEntity.ok(resultados);
    }

    @Operation(summary = "Subir o actualizar la imagen de un conjunto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen subida correctamente"),
            @ApiResponse(responseCode = "404", description = "Conjunto no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> subirImagenConjunto(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        conjuntoService.storeConjuntoImage(id, file);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Descargar la imagen de un conjunto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen descargada correctamente"),
            @ApiResponse(responseCode = "404", description = "Fichero no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}/imagen")
    public ResponseEntity<Resource> descargarImagenConjunto(@PathVariable Long id) throws IOException {
        Resource resource = conjuntoService.loadConjuntoImage(id);
        String contentType = Files.probeContentType(Paths.get(resource.getURI()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}