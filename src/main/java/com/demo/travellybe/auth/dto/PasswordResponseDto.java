package com.demo.travellybe.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordResponseDto {

    private String password;

    public PasswordResponseDto(String password) {
        this.password = password;
    }
}
