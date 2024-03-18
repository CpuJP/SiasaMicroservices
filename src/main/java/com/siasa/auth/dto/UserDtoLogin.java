package com.siasa.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class UserDtoLogin {
    @NotNull
    private String name;

    @NotNull
    private String password;


    public void setPassword(String password) {
        // Validar la contraseña utilizando una expresión regular
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*@#$%^&+=!])(?=\\S+$).{8,}$";
        java.util.regex.Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        // Si la contraseña cumple con los requisitos, establecer el valor
        if (matcher.matches()) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un caracter especial.");
        }
    }
}
