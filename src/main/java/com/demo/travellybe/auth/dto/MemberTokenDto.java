package com.demo.travellybe.auth.dto;

import com.demo.travellybe.auth.domain.MemberTokens;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MemberTokenDto {
    private MemberTokens token;
    private boolean isNewUser;
}
