package com.demo.travellybe.product.dto.response;

import com.demo.travellybe.comment.dto.CommentResponseDto;
import com.demo.travellybe.review.domain.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProductReviewResponseDto {
    private Long reviewId;
    private String reviewUserImage;
    private String reviewUserNickname;
    private List<String> reviewImages;
    private String reviewContent;
    private int rating;
    private LocalDate reviewDate;
    private int likeCount;

    public ProductReviewResponseDto(Review review) {
        this.reviewId = review.getId();
        this.reviewUserImage = review.getMember().getImageUrl();
        this.reviewUserNickname = review.getMember().getNickname();
        this.reviewImages = review.getImageUrls();
        this.reviewContent = review.getContent();
        this.rating = review.getRating();
        this.reviewDate = review.getCreatedDate().toLocalDate();
        this.likeCount = review.getLikeCount();
    }
}
