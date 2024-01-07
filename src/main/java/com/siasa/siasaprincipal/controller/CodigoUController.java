package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.BibliotecaDto;
import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.dto.ErrorResponseDto;
import com.siasa.siasaprincipal.repository.CodigoURepository;
import com.siasa.siasaprincipal.service.CodigoUService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/codigou")
public class CodigoUController {

    private final CodigoUService codigoUService;

    public CodigoUController(CodigoUService codigoUService, CodigoURepository codigoURepository) {
        this.codigoUService = codigoUService;
    }

    @GetMapping
    @Operation(summary = "Get all CodigoU's",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos",
                        content = @Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = CodigoUDto.class)))),
                @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<CodigoUDto>> findAll() {
        return codigoUService.findAll();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Find CodigoU By Id",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Usuario encontrado por ID",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CodigoUDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay usuario con ese ID",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<CodigoUDto> findById(
            @Parameter(name = "id", description = "ID de la persona a buscar",
                in = ParameterIn.PATH, example = "461220346", schema = @Schema(type = "string"))
            @PathVariable String id) {
        return codigoUService.findById(id);
    }

    @GetMapping("/rfid/{idRfid}")
    @Operation(summary = "Find CodigoU By Id of Rfid",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - El usuario con ID de RFID encontrado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CodigoUDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - El ID de RFID no existe",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - El usuario con ID de RFID no registra",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<CodigoUDto> findByRfid(
            @Parameter(name = "idRfid", description = "ID del RFID a buscar",
                    in = ParameterIn.PATH, example = "MN:0L:AA:8T", schema = @Schema(type = "string"))
            @PathVariable String idRfid) {
        return codigoUService.findByRfid(idRfid);
    }


    @PostMapping
    @Operation(summary = "Create a new CodigoU",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created - Usuario creado exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CodigoUDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Revisar campos obligatorios",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - El ID de RFID no registra",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - Un dato que se intenta registrar ya se encuentra vinculado a otra persona",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<CodigoUDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario a registrar",
                required = true,
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CodigoUDto.class)))
            @RequestBody CodigoUDto codigoUDto) {
        return codigoUService.create(codigoUDto);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Update info of CodigoU",
    responses = {
            @ApiResponse(responseCode = "200", description = "OK - Usuario actualizado exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CodigoUDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Revisar campos obligatorios",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - El usuario que se intenta actualizar no registra",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - El ID de RFID no registra",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error inesperado al actualizar la persona",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<CodigoUDto> update(
            @Parameter(name = "id", description = "ID del usuario a actualizar",
                in = ParameterIn.PATH, example = "461220346", schema = @Schema(type = "string"))
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario a actualizar",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CodigoUDto.class)))
            @RequestBody CodigoUDto codigoUDto) {
        return codigoUService.update(id, codigoUDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete all information related to CodigoU",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Eliminación completa de registro existosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay usuario con ese ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<String> deleteAll(
            @Parameter(name = "id", description = "ID del usuario a eliminar registros",
                in = ParameterIn.PATH, example = "461220346", schema = @Schema(type = "string"))
            @PathVariable String id) {
        return codigoUService.delete(id);
    }
}

