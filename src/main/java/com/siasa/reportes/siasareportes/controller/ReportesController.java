package com.siasa.reportes.siasareportes.controller;

import com.siasa.reportes.siasareportes.dto.ReportesDTO;
import com.siasa.reportes.siasareportes.enums.TipoReporte;
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
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportesController {

    private final ReportesService reportesService;

    public ReportesController(ReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @GetMapping(path = "/biblioteca/download")
    public ResponseEntity<Resource> download(@RequestParam Map<String, Object> params)
            throws JRException, IOException, SQLException {
        try {
            ReportesDTO dto = reportesService.obtenerReporte(params, "IngresosBiblioteca");

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
        } catch (ReporteVacioException ex) {
            // Manejar el caso donde el reporte está vacío
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception ex) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}