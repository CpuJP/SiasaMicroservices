package com.siasa.siasaprincipal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CodigoU {

    @Id
    @Column(nullable = false, length = 15)
    private String idCodigoU;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private Rfid rfid;

    @Column(nullable = false, length = 50)
    private String primerNombre;

    @Column(length = 50)
    private String segundoNombre;

    @Column(nullable = false, length = 50)
    private String primerApellido;

    @Column(length = 50)
    private String segundoApellido;


}
