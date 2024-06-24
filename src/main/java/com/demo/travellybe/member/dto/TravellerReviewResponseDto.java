package com.demo.travellybe.member.dto;

import com.demo.travellybe.comment.dto.CommentWithProductDto;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.response.TravellerProductResponseDto;
import com.demo.travellybe.review.domain.Review;
import com.demo.travellybe.review.dto.ReviewWithProductDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TravellerReviewResponseDto {

    // 유저 정보
    private String nickname;
    private String email;
    private String imageUrl;

    // 체험한 상품 리스트
    private List<TravellerProductResponseDto> products;

    // 작성한 후기
    private List<ReviewWithProductDto> reviewWithProducts;

    // 댓글
    private List<CommentWithProductDto> commentWithProducts;

    public TravellerReviewResponseDto(Member member, List<Product> products, List<Review> reviews) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.imageUrl = member.getImageUrl();

        if (!products.isEmpty()) {
            this.products = products.stream().map(TravellerProductResponseDto::new).toList();
        }

        this.reviewWithProducts = reviews.stream().map(ReviewWithProductDto::new).toList();
        this.commentWithProducts = reviews.stream().map(CommentWithProductDto::new).toList();
    }

}
