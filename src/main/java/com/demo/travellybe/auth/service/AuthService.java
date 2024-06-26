package com.demo.travellybe.auth.service;

import com.demo.travellybe.auth.dto.*;

public interface AuthService {

    void signup(SignupRequestDto signupRequestDto);

    MemberTokenDto formLogin(FormRequestDto loginData);

    MemberTokenDto socialLogin(String code, String registrationId);

    TokenResponseDto reissueToken(TokenRequestDto tokenRequestDto);

    void registerRole(String email, String role);

    EmailResponseDto findEmail(String nickname);

    void leave(String password, String userPassword, String email);

    void findPassword(String nickname, String email);
}
