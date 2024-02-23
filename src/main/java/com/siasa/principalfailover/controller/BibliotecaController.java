package com.siasa.principalfailover.controller;

import com.siasa.principalfailover.dto.BibliotecaDto;
import com.siasa.principalfailover.dto.ErrorResponseDto;
import com.siasa.principalfailover.service.BibliotecaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/biblioteca-failover")
@Tag(name = "Biblioteca API", description = "Operaciones relacionadas con el control de acceso a la biblioteca")
public class BibliotecaController {

    private final BibliotecaService bibliotecaService;

    public BibliotecaController(BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
    }


    @GetMapping
    @Operation(summary = "Get all Biblioteca access",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos",
                        content = @Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = BibliotecaDto.class)))),
                @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<BibliotecaDto>> findAll() {
        return bibliotecaService.findAll();
    }


    @GetMapping("/page")
    @Operation(summary = "Get all Biblioteca access in pages",
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
                    in = ParameterIn.QUERY, example = "codigoU.primerApellido", schema = @Schema(type = "string")),
            @Parameter(name = "sortOrder", description = "Orden de la ordenación (asc o desc)",
                    in = ParameterIn.QUERY, example = "asc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<BibliotecaDto>> findALlP(@RequestParam(defaultValue = "0") int pageNumber,
                                                        @RequestParam(defaultValue = "10") int pageSize,
                                                        @RequestParam(defaultValue = "idBiblioteca") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String sortOrder) {
        return bibliotecaService.findAllP(pageNumber, pageSize, sortBy, sortOrder);
    }

    @GetMapping("/codigou/{idCodigoU}")
    @Operation(summary = "Get all access to the Biblioteca by Id CodigoU",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Datos encontrados",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = BibliotecaDto.class))),
                @ApiResponse(responseCode = "400", description = "Bad Request - Datos no existentes",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Not Found - Datos no existentes",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<BibliotecaDto>> findByCodigoU(
            @Parameter(name = "idCodigoU", description = "Id de la persona a buscar",
                in = ParameterIn.PATH, example = "461220346", schema = @Schema(type = "string"))
            @PathVariable String idCodigoU) {
        return bibliotecaService.findByCodigoUIdCodigoU(idCodigoU);
    }

    @GetMapping("/exists/{idCodigoU}")
    @Operation(summary = "Get if there entrance to the Biblioteca",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Usuario con ID existe",
                        content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string"))),
                @ApiResponse(responseCode = "404", description = "Not Found - Usuario con ID NO registra",
                        content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<String> existsByCodigoU(
            @Parameter(name = "idCodigoU", description = "Id de la persona a buscar",
                    in = ParameterIn.PATH, example = "461220346", schema = @Schema(type = "string"))
            @PathVariable String idCodigoU) {
        return bibliotecaService.existsByCodigoUIdCodigoU(idCodigoU);
    }


    @PostMapping("{idRfid}")
    @Operation(summary = "Create the Biblioteca access record",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created - Registro de ingreso creado existosamente",
                    content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = BibliotecaDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - El ID de RFID no registra",
                    content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<BibliotecaDto> create(
            @Parameter(name = "idRfid", description = "ID del carnet a registrar ingreso",
                in = ParameterIn.PATH, example = "MN:0L:AA:8T", schema = @Schema(type = "string"))
            @PathVariable String idRfid) {
        return bibliotecaService.create(idRfid);
    }

    @GetMapping("/fechaingreso")
    @Operation(summary = "Get all access to the Biblioteca By fechaIngreso",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Datos traidos exitosamente",
                        content = @Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = BibliotecaDto.class)))),
                @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        },
        parameters = {
                @Parameter(name = "fechaInicial", description = "Fecha inicial para hacer el filtrado",
                        in = ParameterIn.QUERY, example = "2024-01-01T12:00:00", schema = @Schema(type = "date-time")),
                @Parameter(name = "fechaFinal", description = "Fecha final del rango para hacer el filtrado",
                        in = ParameterIn.QUERY, example = "2024-01-22T12:00:00", schema = @Schema(type = "data-time"))
        })
    public ResponseEntity<List<BibliotecaDto>> findByFechaIngreso(@RequestParam LocalDateTime fechaInicial,
                                                                  @RequestParam LocalDateTime fechaFinal) {
        return bibliotecaService.findByFechaIngreso(fechaInicial, fechaFinal);
    }

    @GetMapping("/idcodigouandfechaingreso")
    @Operation(summary = "Get all access to the Biblioteca By idCodigoU and fechaIngreso",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Datos traidos exitosamente",
                        content = @Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = BibliotecaDto.class)))),
                @ApiResponse(responseCode = "400", description = "Bad Request - ID UDEC no registra",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        },
        parameters = {
                @Parameter(name = "idCodigoU", description = "ID UDEC de usuario para el filtrado",
                        in = ParameterIn.QUERY, example = "461220134", schema = @Schema(type = "string")),
                @Parameter(name = "fechaInicial", description = "Fecha inicial para hacer el filtrado",
                        in = ParameterIn.QUERY, example = "2024-01-01T12:00:00", schema = @Schema(type = "date-time")),
                @Parameter(name = "fechaFinal", description = "Fecha final del rango para hacer el filtrado",
                        in = ParameterIn.QUERY, example = "2024-01-22T12:00:00", schema = @Schema(type = "data-time"))
        })
    public ResponseEntity<List<BibliotecaDto>> findByIdCodigoUAndFechaIngreso(@RequestParam String idCodigoU,
                                                                              @RequestParam LocalDateTime fechaInicial,
                                                                              @RequestParam LocalDateTime fechaFinal) {
        return bibliotecaService.findByIdCodigoUAndFechaIngreso(idCodigoU, fechaInicial, fechaFinal);
    }
}
