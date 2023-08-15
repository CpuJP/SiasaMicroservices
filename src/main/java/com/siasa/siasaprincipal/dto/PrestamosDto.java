package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.enums.OrigenPrestamo;

import java.time.LocalDateTime;

public record PrestamosDto(
        Integer idPrestamos,
        String nombreObjeto,
        LocalDateTime fechaInicioPrestamo,
        LocalDateTime fechaFinalPrestamo,
        OrigenPrestamo origenPrestamo,
        CodigoU codigoU
) {
}
