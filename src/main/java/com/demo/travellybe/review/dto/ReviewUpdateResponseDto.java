package com.demo.travellybe.review.dto;

import com.demo.travellybe.product.dto.OperationDayDto;
import com.demo.travellybe.product.dto.ProductImageDto;
import com.demo.travellybe.review.domain.Review;

import java.time.LocalDate;
import java.util.List;

public class ReviewUpdateResponseDto {
    // 상품 정보
    private Long productId;
    private String productName;
    private List<ProductImageDto> images;
    private List<OperationDayDto> operationDays;

    // 리뷰 정보
    private Long reviewId;
    private int likeCount;
    private List<String> imageUrls;
    private String content;

    public ReviewUpdateResponseDto(Review review) {
        this.productId = review.getProduct().getId();
        this.productName = review.getProduct().getName();
        this.images = review.getProduct().getImages().stream().map(ProductImageDto::new).toList();
        this.operationDays = review.getProduct().getOperationDays().stream().map(OperationDayDto::new).toList();

        this.reviewId = review.getId();
        this.likeCount = review.getLikeCount();
        this.imageUrls  = review.getImageUrls();
        this.content = review.getContent();
    }

}
