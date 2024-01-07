package com.siasa.siasaprincipal.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class RfidDto implements Serializable {
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
