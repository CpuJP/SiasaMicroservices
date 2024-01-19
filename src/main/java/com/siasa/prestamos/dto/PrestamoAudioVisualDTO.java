package com.siasa.prestamos.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PrestamoAudioVisualDTO implements Serializable {

    private Integer idAudioVisual;

    private LocalDateTime fechaPrestamo;

    private LocalDateTime fechaDevolucion;

    private String nota;

    private String observaciones;

    @NotNull
    private String idRfid;

    @NotNull
    private String idUdec;

    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    @NotNull
    private InventarioAudioVisualDTO inventarioAudioVisualDTO;
}
