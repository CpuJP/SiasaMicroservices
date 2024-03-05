package com.siasa.auth.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class TokenDto {

    private String token;

    public TokenDto() {
    }

    public TokenDto(String token) {
        super();
        this.token = token;
    }

}
