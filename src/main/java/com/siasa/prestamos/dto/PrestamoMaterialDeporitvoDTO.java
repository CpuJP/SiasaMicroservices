package com.siasa.prestamos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PrestamoMaterialDeporitvoDTO implements Serializable {

    private Integer idMaterialDeportivo;

    private LocalDateTime fechaPrestamo;

    private LocalDateTime fechaDevolucion;

    @NotNull
    private String idUdec;

    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

    @NotNull
    private InventarioMaterialDeportivoDTO inventarioMaterialDeportivoDTO;
}