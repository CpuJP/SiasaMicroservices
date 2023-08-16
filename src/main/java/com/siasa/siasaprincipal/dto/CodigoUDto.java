package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.Rfid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CodigoUDto implements Serializable {
    private String idCodigoU;

    @NotNull
    private Rfid rfid;

    @NotNull
    private String primerNombre;

    private String segundoNombre;

    @NotNull
    private String primerApellido;

    private String segundoApellido;
}
