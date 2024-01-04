package com.siasa.prestamos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PrestamoAudioVisual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAudioVisual;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaPrestamo;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaDevolucion;

    @Column(nullable = true, length = 150)
    private String nota;

    @Column(nullable = false, length = 15)
    private String idUdec;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_audiovisual")
    private InventarioMaterialDeportivo inventarioMaterialDeportivo;
}
