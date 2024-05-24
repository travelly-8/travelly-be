package com.demo.travellybe.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailResponseDto {

    private String email;

    public EmailResponseDto(String email) {
        this.email = email;
    }
}
