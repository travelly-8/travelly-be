package com.demo.travellybe.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TokenRequestDto {
    private String refreshToken;
}
