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

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private CodigoU codigoU;
}
