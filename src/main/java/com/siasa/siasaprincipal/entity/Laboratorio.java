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
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLaboratorio;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaIngreso;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaSalida;

    //Se crea la relacion 1:1 entre las entidades laboratorio y codigoU
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigou_id_codigou")
    private CodigoU codigoU;
}
