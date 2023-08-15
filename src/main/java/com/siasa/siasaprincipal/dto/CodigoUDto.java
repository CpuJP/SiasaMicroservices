package com.siasa.siasaprincipal.dto;

import com.siasa.siasaprincipal.entity.Rfid;

public record CodigoUDto(
        String idCodigoU,
        Rfid rfid,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido
) {
}
