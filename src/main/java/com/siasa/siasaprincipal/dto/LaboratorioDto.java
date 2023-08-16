package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.CodigoU;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LaboratorioDto implements Serializable {
    private Integer idLaboratorio;

    private LocalDateTime fechaIngreso;

    private LocalDateTime fechaSalida;

    @NotNull
    private CodigoU codigoU;
}
