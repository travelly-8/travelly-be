package com.demo.travellybe.member.controller;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.member.dto.MemberDto;
import com.demo.travellybe.member.service.MemberService;
import com.demo.travellybe.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/my")
    public ResponseEntity<MemberDto> myPage(@AuthenticationPrincipal PrincipalDetails userInfo) {
        MemberDto memberDto = memberService.getUser(userInfo.getUsername());

        return ResponseEntity.ok().body(memberDto);
    }

}
