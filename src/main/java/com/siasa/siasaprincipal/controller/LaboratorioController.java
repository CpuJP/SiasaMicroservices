package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.BibliotecaDto;
import com.siasa.siasaprincipal.dto.ErrorResponseDto;
import com.siasa.siasaprincipal.dto.LaboratorioDto;
import com.siasa.siasaprincipal.entity.Laboratorio;
import com.siasa.siasaprincipal.repository.LaboratorioRepository;
import com.siasa.siasaprincipal.service.LaboratorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/laboratorio")
@Tag(name = "Laboratorio API", description = "Operaciones relacionadas con el control de acceso al laboratorio")
public class LaboratorioController {

    public final LaboratorioService laboratorioService;

    public LaboratorioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    @GetMapping
    @Operation(summary = "Get all Laboratorio access",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LaboratorioDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<LaboratorioDto>> findAll() {
        return laboratorioService.findAll();
    }

    @GetMapping("/page")
    @Operation(summary = "Get all Laboratorio access in pages",
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
                    in = ParameterIn.QUERY, example = "10", schema = @Schema(type = "integer"))
        })
    public ResponseEntity<Page<LaboratorioDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return laboratorioService.findAllP(pageNumber, pageSize);
    }

    @GetMapping("/codigou/{idCodigoU}")
    @Operation(summary = "Get all access to the Laboratorio by Id CodigoU",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Datos encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LaboratorioDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Datos no existentes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - Datos no existentes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<LaboratorioDto>> findByCodigoU(
            @Parameter(name = "idCodigoU", description = "Id de la persona a buscar",
                in = ParameterIn.PATH, example = "461220346", schema = @Schema(type = "string"))
            @PathVariable String idCodigoU) {
        return laboratorioService.findByCodigoUIdCodigoU(idCodigoU);
    }

    @GetMapping("/exists/{idCodigoU}")
    @Operation(summary = "Get if there is entrance to the laboratory",
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
        return laboratorioService.existsByCodigoUIdCodigoU(idCodigoU);
    }

    @PostMapping("{idRfid}")
    @Operation(summary = "Create IN the Laboratory access record",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created - Registro de ingreso creado exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LaboratorioDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - El ID de RFID no registra",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<LaboratorioDto> createIn(
            @Parameter(name = "idRfid", description = "ID del carnet a registrar ingreso",
                in = ParameterIn.PATH, example = "MN:0L:AA:8T", schema = @Schema(type = "string"))
            @PathVariable String idRfid) {
        return laboratorioService.createIn(idRfid);
    }

    @PostMapping("/out/{idRfid}")
    @Operation(summary = "Create OUT the Laboratory access record",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created - Registro de salida creado exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LaboratorioDto.class))),
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
    public ResponseEntity<LaboratorioDto> createOut(
            @Parameter(name = "idRfid", description = "ID del carnet a registrar ingreso",
                in = ParameterIn.PATH, example = "MN:0L:AA:8T", schema = @Schema(type = "string"))
            @PathVariable String idRfid) {
        return laboratorioService.createOut(idRfid);
    }
}