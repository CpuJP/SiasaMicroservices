package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.CodigoU;

import java.time.LocalDateTime;

public record BibliotecaDto(
        Integer idBiblioteca,
        LocalDateTime fechaIngreso,
        CodigoU codigoU
) {
}
