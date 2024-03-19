package com.siasa.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.siasa.auth.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    public void setPassword(String password) {
        // Validar la contraseña utilizando una expresión regular
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        // Si la contraseña cumple con los requisitos, establecer el valor
        if (matcher.matches()) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un caracter especial.");
        }
    }

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;



}
