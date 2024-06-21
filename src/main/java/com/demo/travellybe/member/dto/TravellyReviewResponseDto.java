package com.demo.travellybe.member.dto;

import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.product.dto.OperationDayDto;
import com.demo.travellybe.product.dto.ProductImageDto;
import com.demo.travellybe.review.domain.Review;
import com.demo.travellybe.review.dto.ReviewWithProductDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TravellyReviewResponseDto {

    // 유저 정보
    private String nickname;
    private String email;
    private String imageUrl;

    // 받은 후기
    List<ReviewWithProductDto> reviewWithProducts;

    // 작성한 댓글
    List<TravellyCommentResponseDto> commentWithProducts;

    public TravellyReviewResponseDto(Member member, List<Review> reviews, List<Review> commentReviews) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.imageUrl = member.getImageUrl();

        this.reviewWithProducts = reviews.stream().map(ReviewWithProductDto::new).toList();
        this.commentWithProducts = commentReviews.stream().map(TravellyCommentResponseDto::new).toList();
    }

}
