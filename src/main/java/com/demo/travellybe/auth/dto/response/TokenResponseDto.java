package com.demo.travellybe.auth.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TokenResponseDto {
    private String accessToken;

    public TokenResponseDto(String newAccessToken) {
        this.accessToken = newAccessToken;
    }
}
