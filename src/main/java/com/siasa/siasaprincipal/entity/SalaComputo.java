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

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private CodigoU codigoU;
}
