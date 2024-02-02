package com.siasa.reportes.siasareportes.service;

import com.siasa.reportes.siasareportes.dto.ReportesDTO;

import java.sql.SQLException;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import java.io.IOException;

public interface ReportesService {

    ReportesDTO obtenerReporte(Map<String, Object> params, String reportFileName) throws SQLException, JRException, IOException;
}
