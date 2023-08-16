package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.enums.OrigenPrestamo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PrestamosDto implements Serializable {
    private Integer idPrestamos;

    @NotNull
    private String nombreObjeto;

    private LocalDateTime fechaInicioPrestamo;

    private LocalDateTime fechaFinalPrestamo;

    @NotNull
    private OrigenPrestamo origenPrestamo;

    @NotNull
    private CodigoU codigoU;
}
