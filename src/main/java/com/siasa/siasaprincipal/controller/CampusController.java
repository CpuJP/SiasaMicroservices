package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.CampusDto;
import com.siasa.siasaprincipal.dto.ErrorResponseDto;
import com.siasa.siasaprincipal.service.CampusService;
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
@RequestMapping("/campus")
@Tag(name = "Campus API", description = "Operaciones relacionadas con el control de acceso al campus")
public class CampusController {

    public final CampusService campusService;

    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }


    @GetMapping
    @Operation(summary = "Get all Campus access",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CampusDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<CampusDto>> findAll() {
        return campusService.findAll();
    }


    @GetMapping("/page")
    @Operation(summary = "Get all Campus access in pages",
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
    public ResponseEntity<Page<CampusDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                    @RequestParam(defaultValue = "10") int pageSize,
                                                    @RequestParam(defaultValue = "idCampus") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String sortOrder) {
        return campusService.findAllP(pageNumber, pageSize, sortBy, sortOrder);
    }

    @GetMapping("/codigou/{idCodigoU}")
    @Operation(summary = "Get all access to the campus by Id CodigoU",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Datos encontrados",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = CampusDto.class))),
                @ApiResponse(responseCode = "400", description = "Bad Request - Datos no existentes",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Not Found - Datos no existentes",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<CampusDto>> findByCodigoU(
            @Parameter(name = "idCodigoU", description = "ID de la persona a buscar",
                in = ParameterIn.QUERY, example = "461220346", schema = @Schema(type = "string"))
            @PathVariable String idCodigoU) {
        return campusService.findByCodigoUIdCodigoU(idCodigoU);
    }


    @GetMapping("/exists/{idCodigoU}")
    @Operation(summary = "Get if there is entrance to the campus",
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
        return campusService.existsByCodigoUIdCodigoU(idCodigoU);
    }


    @PostMapping("{idRfid}")
    @Operation(summary = "Create the Campus access record",
        responses = {
                @ApiResponse(responseCode = "201", description = "Created - Registro de ingreso creado existosamente",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = CampusDto.class))),
                @ApiResponse(responseCode = "404", description = "Not Found - El carnet no registra",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<CampusDto> create(
            @Parameter(name = "idRfid", description = "ID del carnet a registrar ingreso",
                    in = ParameterIn.PATH, example = "MN:0L:AA:8T", schema = @Schema(type = "string"))
            @PathVariable String idRfid) {
        return campusService.create(idRfid);
    }
}
