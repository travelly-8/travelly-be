package com.demo.travellybe.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FormRequestDto {
    private String email;
    private String password;
}
