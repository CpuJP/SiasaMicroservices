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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_u_id_codigo_u")
    private CodigoU codigoU;
}
