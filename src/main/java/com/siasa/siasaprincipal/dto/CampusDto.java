package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.CodigoU;

import java.time.LocalDateTime;

public record CampusDto(
        Integer idCampus,
        LocalDateTime fechaIngreso,
        CodigoU codigoU
) {
}
