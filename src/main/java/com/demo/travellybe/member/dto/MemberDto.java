package com.demo.travellybe.member.dto;

import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.review.domain.Review;
import com.demo.travellybe.review.dto.ReviewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private String email;

    private String nickname;

    private int coin;

    private String imageUrl;

    private String role;

    private List<ReviewDto> reviews;

    public MemberDto(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.coin = member.getCoin();
        this.imageUrl = member.getImageUrl();
        this.role = member.getRole().toString().toLowerCase();
        this.reviews = member.getReviews().stream().map(ReviewDto::new).toList();
    }


}
