package com.siasa.siasaprincipal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void setIdRfid(String idRfid) {
        // Validar el formato usando una expresión regular
        String regex = "^[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(idRfid);

        // Si el formato es válido, establecer el valor
        if (matcher.matches()) {
            this.idRfid = idRfid;
        } else {
            throw new IllegalArgumentException("El formato del idRfid no es válido. Debe ser XX:XX:XX:XX.");
        }
    }
}
