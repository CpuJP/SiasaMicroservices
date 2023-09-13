package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.CodigoU;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SalaComputoDto implements Serializable {
    private Integer idSalaComputo;

    private LocalDateTime fechaIngreso;

    private LocalDateTime fechaSalida;

    @NotNull
    private String salaIngreso;

    @NotNull
    private CodigoUDto codigoUDto;
}
