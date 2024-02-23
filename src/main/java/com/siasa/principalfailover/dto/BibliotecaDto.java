package com.siasa.principalfailover.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BibliotecaDto implements Serializable {
    private Integer idBiblioteca;

    @NotNull
    private LocalDateTime fechaIngreso;

    @NotNull
    private CodigoUDto codigoUDto;
}