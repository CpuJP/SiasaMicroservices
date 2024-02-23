package com.siasa.principalfailover.controller;

import com.siasa.principalfailover.dto.ErrorResponseDto;
import com.siasa.principalfailover.dto.SalaComputoDto;
import com.siasa.principalfailover.service.SalaComputoService;
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
@RequestMapping("/salacomputo-failover")
@Tag(name = "Sala Computo API", description = "Operaciones relacionadas con el control de acceso a la Sala de Computo")
public class SalaComputoController {

    public final SalaComputoService salaComputoService;

    public SalaComputoController(SalaComputoService salaComputoService) {
        this.salaComputoService = salaComputoService;
    }

    @GetMapping
    @Operation(summary = "Get all Sala Cómputo access",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SalaComputoDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<SalaComputoDto>> findAll() {
        return salaComputoService.findAll();
    }

    @GetMapping("/page")
    @Operation(summary = "Gett all Sala Cómputo access in pages",
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
    public ResponseEntity<Page<SalaComputoDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                         @RequestParam(defaultValue = "10") int pageSize,
                                                         @RequestParam(defaultValue = "idSalaComputo") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String sortOrder) {
        return salaComputoService.findAllP(pageNumber, pageSize, sortBy, sortOrder);
    }

    @GetMapping("/codigou/{idCodigoU}")
    @Operation(summary = "Get all access to the Sala Cómputo by Id CodigoU",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Datos encontrados",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = SalaComputoDto.class))),
                @ApiResponse(responseCode = "400", description = "Bad Request - Datos no existentes",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Not Found - Datos no existentes",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<SalaComputoDto>> findByCodigoU(
            @Parameter(name = "idCodigoU", description = "Id de la persona a buscar",
                in = ParameterIn.PATH, example = "461220346", schema = @Schema(type = "string"))
            @PathVariable String idCodigoU) {
        return salaComputoService.findByCodigoUIdCodigoU(idCodigoU);
    }

    @GetMapping("/exists/{idCodigoU}")
    @Operation(summary = "Get if there is entrance to the Sala Cómputo",
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
        return salaComputoService.existsByCodigoUIdCodigoU(idCodigoU);
    }

    @PostMapping("{idRfid}")
    @Operation(summary = "Create IN the Sala Cómputo access record",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created - Registro de ingreso creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SalaComputoDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - El ID de RFID no registra",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        },
        parameters = {
            @Parameter(name = "idRfid", description = "ID del carnet a registrar ingreso",
                in = ParameterIn.PATH, example = "MN:0L:AA:8T", schema = @Schema(type = "string")),
            @Parameter(name = "salaDestino", description = "Sala destino",
                in = ParameterIn.QUERY, example = "206", schema = @Schema(type = "string"))
        })
    public ResponseEntity<SalaComputoDto> createIn(@PathVariable String idRfid,
            @RequestParam(defaultValue = "GISTFA") String salaDestino) {
        return salaComputoService.createIn(idRfid, salaDestino);
    }

    @PostMapping("/out/{idRfid}")
    @Operation(summary = "Create OUT the Sala Cómputo access record",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created - Registro de salida creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SalaComputoDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - No registra ningún ingreso a laboratorio",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - El ID de RFID no registra",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - La persona no ha ingresado al laboratorio",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<SalaComputoDto> createOut(
            @Parameter(name = "idRfid", description = "ID del carnet a registrar ingreso",
                in = ParameterIn.PATH, example = "MN:0L:AA:8T", schema = @Schema(type = "string"))
            @PathVariable String idRfid) {
        return salaComputoService.createOut(idRfid);
    }

    @GetMapping("/fechaingreso")
    @Operation(summary = "Get all access to the Sala Cómputo by fechaIngreso",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Datos traidos exitosamente",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SalaComputoDto.class)))),
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
        }
    )
    public ResponseEntity<List<SalaComputoDto>> findByFechaIngreso(@RequestParam LocalDateTime fechaInicial,
                                                                   @RequestParam LocalDateTime fechaFinal) {
        return salaComputoService.findByFechaIngreso(fechaInicial, fechaFinal);
    }

    @GetMapping("/fechasalida")
    @Operation(summary = "Get all access to the Sala Cómputo by fechaSalida",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Datos traidos exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SalaComputoDto.class)))),
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
        }
    )
    public ResponseEntity<List<SalaComputoDto>> findByFechaSalida(@RequestParam LocalDateTime fechaInicial,
                                                                  @RequestParam LocalDateTime fechaFinal) {
        return salaComputoService.findByFechaSalida(fechaInicial, fechaFinal);
    }

    @GetMapping("/idcodigouandfechaingreso")
    @Operation(summary = "Get all access to the Sala Cómputo by idCodigoU and fechaIngreso",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Datos traidos exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SalaComputoDto.class)))),
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
        }
    )
    public ResponseEntity<List<SalaComputoDto>> findByIdCodigoUAndFechaIngreso(@RequestParam String idCodigoU,
                                                                               @RequestParam LocalDateTime fechaInicial,
                                                                               @RequestParam LocalDateTime fechaFinal) {
        return salaComputoService.findByIdCodigoUAndFechaIngreso(idCodigoU, fechaInicial, fechaFinal);
    }

    @GetMapping("/idcodigouandfechasalida")
    @Operation(summary = "Get all access to the Sala Cómputo by idCodigoU and fechaSalida",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Datos traidos exitosamente",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SalaComputoDto.class)))),
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
            }
    )
    public ResponseEntity<List<SalaComputoDto>> findByIdCodigoUAndFechaSalida(@RequestParam String idCodigoU,
                                                                              @RequestParam LocalDateTime fechaInicial,
                                                                              @RequestParam LocalDateTime fechaFinal) {
        return salaComputoService.findByIdCodigoUAndFechaSalida(idCodigoU, fechaInicial, fechaFinal);
    }
}