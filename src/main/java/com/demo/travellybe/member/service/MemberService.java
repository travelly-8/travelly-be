package com.demo.travellybe.member.service;

import com.demo.travellybe.member.dto.ProfileDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    ProfileDto getProfile(String username);

    ProfileDto updateNickname(String username, String nickname);

    ProfileDto updatePassword(String username, String password, String newPassword);

    ProfileDto updateImage(String username, MultipartFile multipartFile);

    ProfileDto updateDefaultImage(String username);
}
