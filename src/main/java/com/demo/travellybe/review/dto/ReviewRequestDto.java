package com.demo.travellybe.review.dto;

import com.demo.travellybe.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequestDto {
    private String content;
    private int rating;
}
