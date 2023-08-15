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

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private CodigoU codigoU;


}
