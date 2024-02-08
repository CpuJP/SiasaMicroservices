package com.siasa.reportes.siasareportes.service;

import com.siasa.reportes.siasareportes.commons.JasperReportManager;
import com.siasa.reportes.siasareportes.dto.ReportesDTO;
import com.siasa.reportes.siasareportes.enums.TipoReporte;
import com.siasa.reportes.siasareportes.exception.ReporteVacioException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@Slf4j
public class ReportesServiceImpl implements ReportesService {

    private final JasperReportService reportService;
    private final DataSource dataSource;

    public ReportesServiceImpl(JasperReportService reportService, DataSource dataSource) {
        this.reportService = reportService;
        this.dataSource = dataSource;
    }

    @Override
    public ReportesDTO obtenerReporte(Map<String, Object> params, String reportFileName)
            throws JRException, IOException, SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime localDateTime = LocalDateTime.now();
        String formattedDateTime = localDateTime.format(formatter);

        String extension = params.get("tipo").toString().equalsIgnoreCase(TipoReporte.EXCEL.name()) ? ".xlsx"
                : ".pdf";

        ByteArrayOutputStream stream = reportService.export(reportFileName, params.get("tipo").toString(), params,
                dataSource.getConnection());

        if (stream.size() <= 911) {
            log.warn("El informe esta vacío");
            throw new ReporteVacioException("El reporte está vacío");
        }

        ReportesDTO dto = new ReportesDTO();
        dto.setFileName(reportFileName + "-" + formattedDateTime + extension);

        byte[] bs = stream.toByteArray();
        dto.setStream(new ByteArrayInputStream(bs));
        dto.setLength(bs.length);

        return dto;
    }
}
