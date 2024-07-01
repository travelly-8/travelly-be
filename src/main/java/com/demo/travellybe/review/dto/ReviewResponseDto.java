package com.demo.travellybe.review.dto;

import com.demo.travellybe.comment.dto.CommentResponseDto;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewResponseDto {

    // 상품 정보
    private Long productId;
    private String productName;
    private int productPrice;

    // 리뷰 정보
    private Long reviewId;
    private String reviewUserImage;
    private String reviewUserNickname;
    private List<String> reviewImages;
    private String reviewContent;
    private int rating;
    private LocalDate reviewDate;
    private int likeCount;

    // 댓글 정보
    private List<CommentResponseDto> comments;

    public ReviewResponseDto(Product product, Review review, Member member, List<CommentResponseDto> commentResponseDtoList) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.productPrice = product.getMaxPrice();

        this.reviewId = review.getId();
        if (member == null) {
            this.reviewUserImage = null;
            this.reviewUserNickname = null;
        } else {
            this.reviewUserImage = member.getImageUrl();
            this.reviewUserNickname = member.getNickname();
        }
        this.reviewImages = review.getImageUrls();
        this.reviewContent = review.getContent();
        this.rating = review.getRating();
        this.reviewDate = review.getCreatedDate().toLocalDate();
        this.likeCount = review.getLikeCount();

        this.comments = commentResponseDtoList;
    }

}
