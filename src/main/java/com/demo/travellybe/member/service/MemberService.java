package com.demo.travellybe.member.service;

import com.demo.travellybe.member.dto.MemberDto;

public interface MemberService {
    public MemberDto getUser(String email);
}
