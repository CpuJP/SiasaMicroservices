package com.siasa.reportes.siasareportes.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayInputStream;

@Getter
@Setter
public class ReportesDTO {

    private String fileName;
    private ByteArrayInputStream stream;
    private int length;
}
