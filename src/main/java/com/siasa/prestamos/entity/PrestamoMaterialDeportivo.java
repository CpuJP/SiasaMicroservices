package com.siasa.prestamos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    private Integer idPrestamoMaterialDeportivo;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaPrestamo;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaDevolucion;

    @Column(nullable = true, length = 150)
    private String nota;

    @Column(nullable = true, length = 150)
    private String observaciones;

    @Column(nullable = false, length = 50)
    private String idRfid;

    @Column(nullable = false, length = 15)
    private String idUdec;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(nullable = false)
    @Min(value = 1)
    private Integer cantidad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_materialdeportivo", nullable = false)
    private InventarioMaterialDeportivo inventarioMaterialDeportivo;
}
