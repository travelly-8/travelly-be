package com.demo.travellybe.review.dto;

import com.demo.travellybe.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {
    private String content;
    private double rating;
    private int likeCount;
    private String imageUrl;

    public ReviewDto(Review review) {
        this.content = review.getContent();
        this.rating = review.getRating();
        this.likeCount = review.getLikeCount();
        this.imageUrl = review.getImageUrl();
    }
}
