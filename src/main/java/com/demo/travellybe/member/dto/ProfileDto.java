package com.demo.travellybe.member.dto;

import com.demo.travellybe.member.domain.Member;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileDto {
    private String email;
    private String nickname;
    private String imageUrl;
    private String type;

    public static ProfileDto of(Member member) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setEmail(member.getEmail());
        profileDto.setNickname(member.getNickname());
        profileDto.setImageUrl(member.getImageUrl());
        profileDto.setType(member.getType());
        return profileDto;
    }

}
