package com.siasa.prestamos.dto;

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

    @NotNull
    private String idUdec;

    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

    @NotNull
    private InventarioAudioVisualDTO inventarioAudioVisualDTO;
}
