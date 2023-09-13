package com.siasa.siasaprincipal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCampus;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaIngreso;

    //Se crea la relacion 1:1 entre las entidades campus y codigoU
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "codigou_id_codigou")
    private CodigoU codigoU;

}
