package com.demo.travellybe.auth.service;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> findUser = memberRepository.findByUsername(username);

        //예외 처리 필요
        if (findUser.isPresent()) {
            return new PrincipalDetails(findUser.get());
        }

        return null;
    }
}
