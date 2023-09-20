package com.siasa.prestamos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class InventarioMaterialDeportivoDTO implements Serializable {

    private Integer idMaterialDeportivo;

    @NotNull
    private String nombre;

    private String descripcion;

    @NotNull
    @Min(0)
    private Integer disponible;

}
