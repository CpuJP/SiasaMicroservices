package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.CodigoU;

import java.time.LocalDateTime;

public record LaboratorioDto(
        Integer idLaboratorio,
        LocalDateTime fechaIngreso,
        LocalDateTime fechaSalida,
        CodigoU codigoU
) {
}
