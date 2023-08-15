package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.CodigoU;

import java.time.LocalDateTime;

public record SalaComputoDto(
        Integer idSalaComputo,
        LocalDateTime fechaIngreso,
        LocalDateTime fechaSalida,
        String salaIngreso,
        CodigoU codigoU
) {
}
