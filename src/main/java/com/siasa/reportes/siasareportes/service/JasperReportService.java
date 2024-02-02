package com.siasa.reportes.siasareportes.service;

import net.sf.jasperreports.engine.JRException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

public interface JasperReportService {
    ByteArrayOutputStream export(String fileName, String tipoReporte, Map<String, Object> params, Connection con) throws JRException, IOException;
}
