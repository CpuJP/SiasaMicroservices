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
public class PrestamoMaterialDeportivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMaterialDeportivo;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaPrestamo;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaDevolucion;

    @Column(nullable = false, length = 15)
    private String idUdec;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_materialdeportivo", nullable = false)
    private InventarioMaterialDeportivo inventarioMaterialDeportivo;
}
