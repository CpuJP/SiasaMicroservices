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
public class Biblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBiblioteca;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaIngreso;

    //Se crea la relacion 1:1 entre las entidades biblioteca y codigoU
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private CodigoU codigoU;


}
