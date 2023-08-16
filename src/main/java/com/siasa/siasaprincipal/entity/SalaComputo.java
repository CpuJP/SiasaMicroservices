package com.siasa.siasaprincipal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SalaComputo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSalaComputo;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaIngreso;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaSalida;

    @Column(nullable = false, length = 10)
    private String salaIngreso;

    //Se crea la relacion 1:1 entre las entidades salaComputo y codigoU
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private CodigoU codigoU;
}
