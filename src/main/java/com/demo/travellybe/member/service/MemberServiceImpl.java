package com.demo.travellybe.member.service;

import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.member.dto.MemberDto;
import com.demo.travellybe.member.dto.ProfileDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public MemberDto getUser(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return new MemberDto(member);
    }

    public ProfileDto getProfile(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return ProfileDto.of(member);
    }

    public ProfileDto updateNickname(String email, String nickname) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.setNickname(nickname);
        memberRepository.save(member);
        return ProfileDto.of(member);
    }

    public ProfileDto updatePassword(String email, String password, String newPassword) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));


        // 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.MEMBER_PASSWORD_MISMATCH);
        }

        member.setPassword(bCryptPasswordEncoder.encode(newPassword));
        memberRepository.save(member);
        return ProfileDto.of(member);
    }
}
