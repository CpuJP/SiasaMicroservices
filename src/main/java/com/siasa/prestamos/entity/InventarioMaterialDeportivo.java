package com.siasa.prestamos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InventarioMaterialDeportivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMaterialDeportivo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = true, length = 150)
    private String descripcion;

    @Column(nullable = false)
    @Min(0)
    private Integer disponible;

}
