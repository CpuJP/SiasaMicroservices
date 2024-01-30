package com.siasa.prestamos.controller;

import com.siasa.prestamos.dto.ErrorResponseDto;
import com.siasa.prestamos.dto.PrestamoAudioVisualDTO;
import com.siasa.prestamos.entity.PrestamoAudioVisual;
import com.siasa.prestamos.service.PrestamoAudioVisualService;
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
@RequestMapping("/prestamoaudiovisual")
@Tag(name = "Prestamo Audio Visual API", description = "Operaciones relacionadas con el control de datos de la trazabilidad de los prestamos")
public class PrestamoAudioVisualController {

    private final PrestamoAudioVisualService prestamoAudioVisualService;

    public PrestamoAudioVisualController(PrestamoAudioVisualService prestamoAudioVisualService) {
        this.prestamoAudioVisualService = prestamoAudioVisualService;
    }

    @GetMapping
    @Operation(summary = "Get all Prestamos Audio Visual",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PrestamoAudioVisualDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findAll() {
        return prestamoAudioVisualService.findAll();
    }

    @GetMapping("/page")
    @Operation(summary = "Get all Prestamos Audio Visual in pages",
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
                        in = ParameterIn.QUERY, example = "inventarioAudioVisual.nombre", schema = @Schema(type = "string")),
                @Parameter(name = "sortOrder", description = "Orden de la ordenación (asc o desc)",
                        in = ParameterIn.QUERY, example = "asc", schema = @Schema(type = "string"))
        })
    public ResponseEntity<Page<PrestamoAudioVisualDTO>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                                 @RequestParam(defaultValue = "idPrestamoAudioVisual") String sortBy,
                                                                 @RequestParam(defaultValue = "asc") String sortOrder) {
        return prestamoAudioVisualService.findAllP(pageNumber, pageSize, sortBy, sortOrder);
    }

    @PostMapping("/in")
    @Operation(summary = "Create IN traceability of Audio Visual Loans",
        responses = {
                @ApiResponse(responseCode = "201", description = "Created - Prestamo inicial creado exitosamente",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = PrestamoAudioVisualDTO.class))),
                @ApiResponse(responseCode = "400", description = "Bad Request - Campos obligatorios requeridos, y la cantidad a prestar no puede ser inferior a 1, o El carnet no registra en base de datos, o Error interno de la API consumida",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Not Found - El objeto que se intenta prestar no registra, o La persona no registra",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "409", description = "Conflict - El objeto no cuenta con suficiente stock para realizar el prestamo",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor, o Error interno del servidor de la API consumida",
                        content = @Content(schema = @Schema(hidden = true)))
        },
        parameters = {
            @Parameter(name = "idInventarioAudioVisual", description = "ID del objeto a prestar para vinculación",
                in = ParameterIn.QUERY, example = "2", schema = @Schema(type = "integer")),
            @Parameter(name = "idRfid", description = "ID del carnet para realizar vinculación del objeto con la persona",
                in = ParameterIn.QUERY, example = "MN:0L:AA:8T", schema = @Schema(type = "string")),
            @Parameter(name = "nota", description = "Campo para agregar algún comentario al realizar el prestamo del objeto",
                in = ParameterIn.QUERY, example = "Control sin baterias", schema = @Schema(type = "string")),
            @Parameter(name = "cantidad", description = "Cantidad del objeto en unidades para realizar el prestamo",
                in = ParameterIn.QUERY, example = "3", schema = @Schema(type = "integer"))
        })
    public ResponseEntity<PrestamoAudioVisualDTO> createIn(@RequestParam Integer idInventarioAudioVisual,
                                                           @RequestParam String idRfid,
                                                           @RequestParam(required = false) String nota,
                                                           @RequestParam Integer cantidad) {
        return prestamoAudioVisualService.createIn(idInventarioAudioVisual, idRfid, nota, cantidad);
    }

    @PostMapping("/out")
    @Operation(summary = "Create OUT traceability of Audio Visual Loans",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created - Devolución de Prestamo creado exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PrestamoAudioVisual.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - Prestamo con ID no registra o el objeto no registra con ID en el inventario",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        },
        parameters = {
            @Parameter(name = "idPrestamoAudioVisual", description = "Id del prestamo ya realizado para asignar la devolución",
                in = ParameterIn.QUERY, example = "1", schema = @Schema(type = "integer")),
            @Parameter(name = "observaciones", description = "Observaciones o anotaciones respecto al estado de la devolución del objeto",
                in = ParameterIn.QUERY, example = "Control dañado en una esquina", schema = @Schema(type = "string"))
        })
    public ResponseEntity<PrestamoAudioVisualDTO> createOut (@RequestParam Integer idPrestamoAudioVisual,
                                                             @RequestParam(required = false) String observaciones) {
        return prestamoAudioVisualService.createOut(idPrestamoAudioVisual, observaciones);
    }

    @GetMapping("/rfid/{idRfid}")
    @Operation(summary = "Get all Prestamos Audio Visual By ID Rfid",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista traida con prestamos filtrados por ID Rfid",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PrestamoAudioVisual.class)))),
            @ApiResponse(responseCode = "400", description = "Error al obtener la información de la API externa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista filtrada por prestamos con ID Rfid o La API externa no se encuentra",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByIdRfid(
            @Parameter(name = "idRfid", description = "ID de Rfid para filtrar lista de prestamos",
                in = ParameterIn.PATH, example = "MN:0L:AA:8T", schema = @Schema(type = "string"))
            @PathVariable String idRfid) {
        return prestamoAudioVisualService.findAllByIdRfid(idRfid);
    }

    @GetMapping("/codigou/{idUdec}")
    @Operation(summary = "Get all Prestamos Audio Visual By ID UdeC",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Lista taida con prestamos filtrados por ID UdeC",
                        content = @Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = PrestamoAudioVisual.class)))),
                @ApiResponse(responseCode = "400", description = "Error al obtener la información de la API externa",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista filtrada por prestamos con ID UdeC o La API externa no se encuentra",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByIdUdec(
            @Parameter(name = "idUdec", description = "ID de UdeC para filtrar lista de prestamos",
                    in = ParameterIn.PATH, example = "461220148", schema = @Schema(type = "string"))
            @PathVariable String idUdec) {
        return prestamoAudioVisualService.findAllByIdUdec(idUdec);
    }

    @GetMapping("/fechaprestamo")
    @Operation(summary = "Get all the AudioVisual Loans with a range of two dates By fechaPrestamo",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos filtrada por un rango de fechas",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PrestamoAudioVisual.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista filtrada por rando de fechas",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        },
        parameters = {
            @Parameter(name = "fechaInicial", description = "fechaInicial del rango de filtrado",
                in = ParameterIn.QUERY, example = "2024-01-22T12:00:00", schema = @Schema(type = "data-time")),
            @Parameter(name = "fechaFinal", description = "fechaFinal del rango de filtrado",
                in = ParameterIn.QUERY, example = "2024-01-30T12:00:00", schema = @Schema(type = "data-time"))
        })
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaPrestamo(@RequestParam LocalDateTime fechaInicial,
                                                                            @RequestParam LocalDateTime fechaFinal) {
        return prestamoAudioVisualService.findByFechaPrestamo(fechaInicial, fechaFinal);
    }

    @GetMapping("/fechadevolucion")
    @Operation(summary = "Get all the AudioVisual Loans with a range of two dates By fechaDevolución",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos filtrada por un rango de fechas",
                        content = @Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = PrestamoAudioVisual.class)))),
                @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista filtrada por rando de fechas",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        },
        parameters = {
                @Parameter(name = "fechaInicial", description = "fechaInicial del rango de filtrado",
                        in = ParameterIn.QUERY, example = "2024-01-22T12:00:00", schema = @Schema(type = "data-time")),
                @Parameter(name = "fechaFinal", description = "fechaFinal del rango de filtrado",
                        in = ParameterIn.QUERY, example = "2024-01-30T12:00:00", schema = @Schema(type = "data-time"))
        })
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaDevolucion(@RequestParam LocalDateTime fechaInicial,
                                                                            @RequestParam LocalDateTime fechaFinal) {
        return prestamoAudioVisualService.findByFechaDevolucion(fechaInicial, fechaFinal);
    }

    @GetMapping("/devolucionempty")
    @Operation(summary = "Get all Prestamos Audio Visual when fechaDevolución is Empty",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos filtrada cuando fechaDevolución este vacia",
                        content = @Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = PrestamoAudioVisual.class)))),
                @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista filtrada cuando fechaDevolución este vacia",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaDevolucionIsEmpty() {
        return prestamoAudioVisualService.findByFechaDevolucionIsEmpty();
    }

    @GetMapping("/inventario/{nombreObjeto}")
    @Operation(summary = "Get all Prestamos Audio Visual By nombreObjeto",
        responses = {
                @ApiResponse(responseCode = "200", description = "OK - Lista traida con datos filtrada por nombre de objeto",
                        content = @Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = PrestamoAudioVisual.class)))),
                @ApiResponse(responseCode = "404", description = "Not Found - No hay datos en la lista filtrada por nombre de objeto",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDto.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                        content = @Content(schema = @Schema(hidden = true)))
        })
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByInventarioAudioVisualNombreBeLike(
            @Parameter(name = "nombreObjeto", description = "Nombre del objeto para filtrar la lista de prestamos",
                in = ParameterIn.PATH, example = "HDMI", schema = @Schema(type = "string"))
            @PathVariable String nombreObjeto) {
        return prestamoAudioVisualService.findByInventarioAudioVisualNombreBeLike(nombreObjeto);
    }
}
