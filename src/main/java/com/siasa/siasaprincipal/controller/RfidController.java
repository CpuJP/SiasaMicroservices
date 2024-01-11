package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.BibliotecaDto;
import com.siasa.siasaprincipal.dto.ErrorResponseDto;
import com.siasa.siasaprincipal.dto.RfidDto;
import com.siasa.siasaprincipal.service.RfidService;
//import io.swagger.v3.oas.annotations.OpenAPI31;
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

import java.util.List;

@RestController
@RequestMapping("/rfid")
@Tag(name = "Rfid API", description = "Operaciones relacionadas con el control de datos de rfid")
public class RfidController {

    private final RfidService rfidService;

    public RfidController(RfidService rfidService) {
        this.rfidService = rfidService;
    }

    //se crea el endpoint get principal de la entidad rfid donde se traen todos los datos de la BD

    @GetMapping
    @Operation(summary = "Get all Rfids",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RfidDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<RfidDto>> findAll() {
        //se utiliza el service de la implementacion en el endpoint para no cargar la
        // parte de transferencia con logica de negocio
        return rfidService.findAll();
    }

    @GetMapping("/page")
    @Operation(summary = "Get all Rfid registers in pages",
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
    public ResponseEntity<Page<RfidDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
        return rfidService.findAllP(pageNumber, pageSize);
    }

    @GetMapping("/without")
    @Operation(summary = "Get all Rfid without U code",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista traida con Rfid sin asignar a usuario",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RfidDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay Rfid sin asignar",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<RfidDto>> findRfidWithoutCodigoU() {
        return rfidService.findRfidWithoutCodigoU();
    }

    //se crea el endpoint post con la ruta /{id} donde se envia el id desde la api al server
    @PostMapping("/{id}")
    @Operation(summary = "Find Rfid by Id",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Datos encontrados",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RfidDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - ID de RFID no registra",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<RfidDto> findById(
            @Parameter(name = "id", description = "ID del carnet a buscar",
                    in = ParameterIn.PATH, example = "MN:0L:AA:8T", schema = @Schema(type = "string"))
            @PathVariable String id) {
        //se utiliza el service de la implementacion en el endpoint para no cargar la
        // parte de transferencia con logica de negocio
        return rfidService.findById(id);
    }

    // Se crea el endpoint post principal para la creción de nuevos datos en BD de
    // la entidad Rfid
    @PostMapping
    @Operation(summary = "Create a new Rfid",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created - El carnet se registró exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RfidDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - El ID del RFID es obligatorio",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - El ID del RFID ya de encuentra registrado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<RfidDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del RFID a registrar",
                required = true,
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = RfidDto.class)))
            @RequestBody RfidDto rfidDto) {
        return rfidService.create(rfidDto);
    }

    // Se crea el endpoint delete para borrar un carnet ya existente en la base de datos.
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Rfid by Id",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Registro de RFID eliminado exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "Not Found - EL ID del RFID no registra",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - El ID del RFID tiene datos ligados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<String> delete(
            @Parameter(name = "id", description = "ID del RFID a eliminar del registro",
                in = ParameterIn.PATH, example = "MN:0L:AA:8T", schema = @Schema(type = "string"))
            @PathVariable String id) {
        return rfidService.delete(id);
    }
}
