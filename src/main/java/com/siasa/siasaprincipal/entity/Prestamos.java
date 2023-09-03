package com.siasa.siasaprincipal.entity;

import com.siasa.siasaprincipal.enums.OrigenPrestamo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Prestamos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPrestamos;

    @Column(nullable = false,length = 50)
    private String nombreObjeto;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaInicioPrestamo;

    @Column(nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaFinalPrestamo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigenPrestamo origenPrestamo;

    //Se crea la relacion 1:M entre las entidades prestamos y codigoU
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigou_id_codigou")
    private CodigoU codigoU;
}
