package com.siasa.reportes.siasareportes.controller;

import com.siasa.reportes.siasareportes.dto.ErrorResponseDto;
import com.siasa.reportes.siasareportes.dto.ReportesDTO;
import com.siasa.reportes.siasareportes.enums.TipoReporte;
import com.siasa.reportes.siasareportes.exception.MessageBadRequestException;
import com.siasa.reportes.siasareportes.exception.ReporteVacioException;
import com.siasa.reportes.siasareportes.service.ReportesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@RestController
@RequestMapping("/report")
@Tag(name = "Reportes API", description = "Generación de reportes de las diferentes áreas de ingreso")
public class ReportesController {

    private final ReportesService reportesService;

    public ReportesController(ReportesService reportesService) {
        this.reportesService = reportesService;
    }

    private ResponseEntity<Resource> getResourceResponseEntity(@RequestParam Map<String, Object> params, ReportesDTO dto) {
        if (dto.getLength() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        InputStreamResource streamResource = new InputStreamResource(dto.getStream());
        MediaType mediaType = (params.get("tipo").toString().equalsIgnoreCase(TipoReporte.EXCEL.name())) ?
                MediaType.APPLICATION_OCTET_STREAM : MediaType.APPLICATION_PDF;

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
                .contentLength(dto.getLength())
                .contentType(mediaType)
                .body(streamResource);
    }

    @GetMapping(path = "/biblioteca/download")
    @Operation(summary = "Download library admission report",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK - Reporte generado exitosamente",
                content = @Content(mediaType = "application/octet-stream",
                    schema = @Schema(type = "string",
                        format = "binary"))),
            @ApiResponse(responseCode = "204", description = "Not Content - El informa generado esta vacío",
                content = @Content(mediaType = "",
                    schema = @Schema)),
            @ApiResponse(responseCode = "400", description = "Bad Request - El formato de fecha no es el correcto (yyyy-MM-dd) o esta vacía",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                    content = @Content(schema = @Schema(hidden = true)))
        },
        parameters = {
                @Parameter(name = "tipo", description = "Tipo de reporte (PDF o EXCEL)",
                        in = ParameterIn.QUERY, example = "PDF", schema = @Schema(type = "string")),
                @Parameter(name = "fechaInicial", description = "Fecha inicial del reporte (yyyy-MM-dd)",
                        in = ParameterIn.QUERY, example = "2024-01-01", required = true, schema = @Schema(type = "string", format = "date")),
                @Parameter(name = "fechaFinal", description = "Fecha final del reporte (yyyy-MM-dd)"
                        , in = ParameterIn.QUERY, example = "2024-01-31", required = true, schema = @Schema(type = "string", format = "date"))
        })
    public ResponseEntity<Resource> downloadBiblioteca(
            @Parameter(hidden = true)
            @RequestParam Map<String, Object> params)
            throws JRException, IOException, SQLException {
        Object fechaInicialObj = params.get("fechaInicial");
        Object fechaFinalObj = params.get("fechaFinal");
        if (fechaFinalObj != null || fechaInicialObj != null && fechaFinalObj instanceof String || fechaInicialObj instanceof String) {
            String fechaInicialStr = (String) fechaInicialObj;
            String fechaFinalStr = (String) fechaFinalObj;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(fechaInicialStr);
                dateFormat.parse(fechaFinalStr);

                try {
                    ReportesDTO dto = reportesService.obtenerReporte(params, "IngresosBiblioteca");

                    return getResourceResponseEntity(params, dto);
                } catch (ReporteVacioException ex) {
                    // Manejar el caso donde el reporte está vacío
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
                } catch (Exception ex) {
                    // Manejar otros errores
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }

            } catch (ParseException e) {
                throw new MessageBadRequestException("La fecha ingresada no cumple con el formato requerido (yyyy-MM-dd)");
            }
        } else {
            throw new MessageBadRequestException("La fechaInicial y la fechaFinal esta vacia");
        }
    }

    @GetMapping(path = "/laboratorio/download")
    @Operation(summary = "Download admission report to the Laboratory",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Reporte generado exitosamente",
                            content = @Content(mediaType = "application/octet-stream",
                                    schema = @Schema(type = "string",
                                            format = "binary"))),
                    @ApiResponse(responseCode = "204", description = "Not Content - El informa generado esta vacío",
                            content = @Content(mediaType = "",
                                    schema = @Schema)),
                    @ApiResponse(responseCode = "400", description = "Bad Request - El formato de fecha no es el correcto (yyyy-MM-dd) o esta vacía",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                            content = @Content(schema = @Schema(hidden = true)))
            },
            parameters = {
                    @Parameter(name = "tipo", description = "Tipo de reporte (PDF o EXCEL)",
                            in = ParameterIn.QUERY, example = "PDF", schema = @Schema(type = "string")),
                    @Parameter(name = "fechaInicial", description = "Fecha inicial del reporte (yyyy-MM-dd)",
                            in = ParameterIn.QUERY, example = "2024-01-01", required = true, schema = @Schema(type = "string", format = "date")),
                    @Parameter(name = "fechaFinal", description = "Fecha final del reporte (yyyy-MM-dd)"
                            , in = ParameterIn.QUERY, example = "2024-01-31", required = true, schema = @Schema(type = "string", format = "date"))
            })
    public ResponseEntity<Resource> downloadLaboratorio(
            @Parameter(hidden = true)
            @RequestParam Map<String, Object> params)
        throws JRException, IOException, SQLException {
        Object fechaInicialObj = params.get("fechaInicial");
        Object fechaFinalObj = params.get("fechaFinal");
        if (fechaFinalObj != null || fechaInicialObj != null && fechaFinalObj instanceof String || fechaInicialObj instanceof String) {
            String fechaInicialStr = (String) fechaInicialObj;
            String fechaFinalStr = (String) fechaFinalObj;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(fechaInicialStr);
                dateFormat.parse(fechaFinalStr);
                try {
                    ReportesDTO dto = reportesService.obtenerReporte(params, "IngresosLaboratorio");

                    return getResourceResponseEntity(params, dto);
                } catch (ReporteVacioException ex) {
                    // Manejar el caso donde el reporte está vacío
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
                } catch (Exception ex) {
                    // Manejar otros errores
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }

            } catch (ParseException e) {
                throw new MessageBadRequestException("La fecha ingresada no cumple con el formato requerido (yyyy-MM-dd)");
            }
        } else {
            throw new MessageBadRequestException("La fechaInicial y la fechaFinal esta vacia");
        }
    }

    @GetMapping(path = "/campus/download")
    @Operation(summary = "Download Campus admission report",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Reporte generado exitosamente",
                            content = @Content(mediaType = "application/octet-stream",
                                    schema = @Schema(type = "string",
                                            format = "binary"))),
                    @ApiResponse(responseCode = "204", description = "Not Content - El informa generado esta vacío",
                            content = @Content(mediaType = "",
                                    schema = @Schema)),
                    @ApiResponse(responseCode = "400", description = "Bad Request - El formato de fecha no es el correcto (yyyy-MM-dd) o esta vacía",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                            content = @Content(schema = @Schema(hidden = true)))
            },
            parameters = {
                    @Parameter(name = "tipo", description = "Tipo de reporte (PDF o EXCEL)",
                            in = ParameterIn.QUERY, example = "PDF", schema = @Schema(type = "string")),
                    @Parameter(name = "fechaInicial", description = "Fecha inicial del reporte (yyyy-MM-dd)",
                            in = ParameterIn.QUERY, example = "2024-01-01", required = true, schema = @Schema(type = "string", format = "date")),
                    @Parameter(name = "fechaFinal", description = "Fecha final del reporte (yyyy-MM-dd)"
                            , in = ParameterIn.QUERY, example = "2024-01-31", required = true, schema = @Schema(type = "string", format = "date"))
            })
    public ResponseEntity<Resource> downloadCampus(
            @Parameter(hidden = true)
            @RequestParam Map<String, Object> params)
        throws JRException, IOException, SQLException {
        Object fechaInicialObj = params.get("fechaInicial");
        Object fechaFinalObj = params.get("fechaFinal");
        if (fechaFinalObj != null || fechaInicialObj != null && fechaFinalObj instanceof String || fechaInicialObj instanceof String) {
            String fechaInicialStr = (String) fechaInicialObj;
            String fechaFinalStr = (String) fechaFinalObj;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(fechaInicialStr);
                dateFormat.parse(fechaFinalStr);

                try {
                    ReportesDTO dto = reportesService.obtenerReporte(params, "IngresosCampus");

                    return getResourceResponseEntity(params, dto);
                } catch (ReporteVacioException ex) {
                    // Manejar el caso donde el reporte está vacío
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
                } catch (Exception ex) {
                    // Manejar otros errores
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }

            } catch (ParseException e) {
                throw new MessageBadRequestException("La fecha ingresada no cumple con el formato requerido (yyyy-MM-dd)");
            }
        } else {
            throw new MessageBadRequestException("La fechaInicial y la fechaFinal esta vacia");
        }
    }

    @GetMapping(path = "/salacomputo/download")
    @Operation(summary = "Download admission report to the Computer Room",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Reporte generado exitosamente",
                            content = @Content(mediaType = "application/octet-stream",
                                    schema = @Schema(type = "string",
                                            format = "binary"))),
                    @ApiResponse(responseCode = "204", description = "Not Content - El informa generado esta vacío",
                            content = @Content(mediaType = "",
                                    schema = @Schema)),
                    @ApiResponse(responseCode = "400", description = "Bad Request - El formato de fecha no es el correcto (yyyy-MM-dd) o esta vacía",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - Error interno del servidor",
                            content = @Content(schema = @Schema(hidden = true)))
            },
            parameters = {
                    @Parameter(name = "tipo", description = "Tipo de reporte (PDF o EXCEL)",
                            in = ParameterIn.QUERY, example = "PDF", schema = @Schema(type = "string")),
                    @Parameter(name = "fechaInicial", description = "Fecha inicial del reporte (yyyy-MM-dd)",
                            in = ParameterIn.QUERY, example = "2024-01-01", required = true, schema = @Schema(type = "string", format = "date")),
                    @Parameter(name = "fechaFinal", description = "Fecha final del reporte (yyyy-MM-dd)"
                            , in = ParameterIn.QUERY, example = "2024-01-31", required = true, schema = @Schema(type = "string", format = "date"))
            })
    public ResponseEntity<Resource> downloadSalaComputo(
            @Parameter(hidden = true)
            @RequestParam Map<String, Object> params)
        throws JRException, IOException, SQLException {
        Object fechaInicialObj = params.get("fechaInicial");
        Object fechaFinalObj = params.get("fechaFinal");
        if (fechaFinalObj != null || fechaInicialObj != null && fechaFinalObj instanceof String || fechaInicialObj instanceof String) {
            String fechaInicialStr = (String) fechaInicialObj;
            String fechaFinalStr = (String) fechaFinalObj;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(fechaInicialStr);
                dateFormat.parse(fechaFinalStr);

                try {
                    ReportesDTO dto = reportesService.obtenerReporte(params, "IngresosSalaComputo");

                    return getResourceResponseEntity(params, dto);
                } catch (ReporteVacioException ex) {
                    // Manejar el caso donde el reporte está vacío
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
                } catch (Exception ex) {
                    // Manejar otros errores
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }

            } catch (ParseException e) {
                throw new MessageBadRequestException("La fecha ingresada no cumple con el formato requerido (yyyy-MM-dd)");
            }
        } else {
            throw new MessageBadRequestException("La fechaInicial y la fechaFinal esta vacia");
        }
    }

}