package com.demo.travellybe.member.service;

import com.demo.travellybe.member.dto.MemberDto;
import com.demo.travellybe.member.dto.ProfileDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    MemberDto getUser(String email);

    ProfileDto getProfile(String username);

    ProfileDto updateNickname(String username, String nickname);

    ProfileDto updatePassword(String username, String password, String newPassword);

    ProfileDto updateImage(String username, MultipartFile multipartFile);
}
