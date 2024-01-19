package com.siasa.prestamos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CodigoUDTO {
    private String idCodigoU;

    @NotNull
    private RfidDTO rfidDto;

    @NotNull
    private String primerNombre;

    private String segundoNombre;

    @NotNull
    private String primerApellido;

    private String segundoApellido;

    public static class RfidDTO {
        private String idRfid;
    }
}
