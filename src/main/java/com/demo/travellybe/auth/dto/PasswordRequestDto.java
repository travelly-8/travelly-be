package com.demo.travellybe.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordRequestDto {
    private String nickname;
    private String email;
}
