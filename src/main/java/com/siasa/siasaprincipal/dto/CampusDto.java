package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.CodigoU;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CampusDto implements Serializable {
    private Integer idCampus;

    @NotNull
    private LocalDateTime fechaIngreso;

    @NotNull
    private CodigoUDto codigoUDto;
}
