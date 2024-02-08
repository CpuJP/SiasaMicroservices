package com.siasa.reportes.siasareportes.controller;

import com.siasa.reportes.siasareportes.dto.ReportesDTO;
import com.siasa.reportes.siasareportes.enums.TipoReporte;
import com.siasa.reportes.siasareportes.exception.MessageBadRequestException;
import com.siasa.reportes.siasareportes.exception.ReporteVacioException;
import com.siasa.reportes.siasareportes.service.ReportesService;
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
    public ResponseEntity<Resource> downloadBiblioteca(@RequestParam Map<String, Object> params)
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
    public ResponseEntity<Resource> downloadLaboratorio(@RequestParam Map<String, Object> params)
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
    public ResponseEntity<Resource> downloadCampus(@RequestParam Map<String, Object> params)
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
    public ResponseEntity<Resource> downloadSalaComputo(@RequestParam Map<String, Object> params)
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