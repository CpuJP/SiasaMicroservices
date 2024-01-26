package com.siasa.prestamos.controller;

import com.siasa.prestamos.dto.ErrorResponseDto;
import com.siasa.prestamos.dto.InventarioAudioVisualDTO;
import com.siasa.prestamos.dto.InventarioMaterialDeportivoDTO;
import com.siasa.prestamos.service.InventarioMaterialDeportivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materialdeportivo")
@Tag(name = "Inventario Material Deportivo API", description = "Operaciones realcionadas con el control de datos del inventario")
public class InventarioMaterialDeportivoController {

    private final InventarioMaterialDeportivoService inventarioMaterialDeportivoService;

    public InventarioMaterialDeportivoController(InventarioMaterialDeportivoService inventarioMaterialDeportivoService) {
        this.inventarioMaterialDeportivoService = inventarioMaterialDeportivoService;
    }

    @GetMapping
    @Operation(summary = "Get all Material Deportivo",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK- Lista traida con datos",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = InventarioMaterialDeportivoDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<InventarioMaterialDeportivoDTO>> findAll() {
        return inventarioMaterialDeportivoService.findAll();
    }

    @GetMapping("/page")
    @Operation(summary = "Get all Material Deportivo objects in pages",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos paginados"),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista paginada",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        },
        parameters = {
                @Parameter(name = "pageNumber", description = "Número de página. Por defecto: 0",
                        in = ParameterIn.QUERY, example = "0", schema = @Schema(type = "integer")),
                @Parameter(name = "pageSize", description = "Tamaño de la página. Por defecto: 10",
                        in = ParameterIn.QUERY, example = "10", schema = @Schema(type = "integer")),
                @Parameter(name = "sortBy", description = "Campo por el cual ordenar la lista",
                        in = ParameterIn.QUERY, example = "nombre", schema = @Schema(type = "string")),
                @Parameter(name = "sortOrder", description = "Orden de la ordenación (asc o desc)",
                        in = ParameterIn.QUERY, example = "asc", schema = @Schema(type = "string"))
        })
    public ResponseEntity<Page<InventarioMaterialDeportivoDTO>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                                         @RequestParam(defaultValue = "10") int pageSize,
                                                                         @RequestParam(defaultValue = "idMaterialDeportivo") String sortBy,
                                                                         @RequestParam(defaultValue = "asc") String sortOrder) {
        return inventarioMaterialDeportivoService.findAllP(pageNumber, pageSize, sortBy, sortOrder);
    }


    @GetMapping("/{nombre}")
    @Operation(summary = "Get all Material Deportivo by Name",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Objeto encontrado por el nombre",
                        content =  @Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = InventarioMaterialDeportivoDTO.class)))),
                @ApiResponse(responseCode = "404", description = "Not Found - No hay objetos con ese nombre",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<InventarioMaterialDeportivoDTO>> findByNombre(
            @Parameter(name = "nombre", description = "Nombre de la lista de objetos a buscar",
                    in = ParameterIn.PATH, example = "balon", schema = @Schema(type = "String"))
            @PathVariable String nombre) {
        return inventarioMaterialDeportivoService.findByNombre(nombre);
    }

    @PostMapping
    @Operation(summary = "Create a new Object in Material Deportivo",
        responses = {
                @ApiResponse(responseCode = "201", description = "Created - Objeto creado exitosamente",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = InventarioMaterialDeportivoDTO.class))),
                @ApiResponse(responseCode = "400", description = "Bad Request - Campos obligatorios requeridos, y la cantida no puede ser inferior a 0",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "409", description = "Conflict - El nombre del objeto ya registra en el inventario",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<InventarioMaterialDeportivoDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del objeto a registrar",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioAudioVisualDTO.class),
                            examples = {
                                    @ExampleObject(name = "Ejemplo de Creación",
                                            description = "Este es un ejemplo del RequestBody que debe llegar al EndPoint",
                                            value = "{\n" +
                                                    "  \"nombre\": \"Balon de Baloncesto\",\n" +
                                                    "  \"descripcion\": \"Balon de baloncesto marca molten\",\n" +
                                                    "  \"disponible\": 5\n" +
                                                    "}")
                            }))
            @RequestBody InventarioMaterialDeportivoDTO materialDeportivoDTO) {
        return inventarioMaterialDeportivoService.create(materialDeportivoDTO);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Update info to InventarioMaterialDeportivo",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Objeto actualizado exitosamente",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = InventarioMaterialDeportivoDTO.class))),
                @ApiResponse(responseCode = "400", description = "Bad Request - Campos obligatorios requeridos, y la cantidad no puede ser inferior a 0",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Not Found - El objeto que se intenta actualizar no regista con ese id",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado al actualizar la persona",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class)))
        })
    public ResponseEntity<InventarioMaterialDeportivoDTO> update(
            @Parameter(name = "id", description = "ID del objeto a actualizar",
                    in = ParameterIn.PATH, example = "1", schema = @Schema(type = "integer"))
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del objeto a actualizar",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioAudioVisualDTO.class),
                            examples = {
                                    @ExampleObject(name = "Ejemplo de Actualización",
                                            description = "Este es un ejemplo del RequestBody que debe llegar al EndPoint",
                                            value = "{\n" +
                                                    "  \"nombre\": \"Balon de Baloncesto\",\n" +
                                                    "  \"descripcion\": \"Balon de baloncesto marca molten\",\n" +
                                                    "  \"disponible\": 5\n" +
                                                    "}")
                            }))
            @RequestBody InventarioMaterialDeportivoDTO materialDeportivoDTO) {
        return inventarioMaterialDeportivoService.updtae(id, materialDeportivoDTO);
    }
}
