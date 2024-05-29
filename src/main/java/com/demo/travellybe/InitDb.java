package com.demo.travellybe;

import com.demo.travellybe.auth.dto.SignupRequestDto;
import com.demo.travellybe.auth.service.AuthService;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.service.MemberService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final AuthService authService;

        public void init() {
            SignupRequestDto signupRequestDto = new SignupRequestDto();
            signupRequestDto.setEmail("test1234@gmail.com");
            signupRequestDto.setPassword("test1234!");
            signupRequestDto.setNickname("test1234");

            authService.signup(signupRequestDto);
        }
    }
}
