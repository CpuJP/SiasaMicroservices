package com.siasa.siasaprincipal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Rfid {

    @Id
    @Column(nullable = false, length = 50)
    private String idRfid;
}
