package com.demo.travellybe.review.dto;

import com.demo.travellybe.product.dto.OperationDayDto;
import com.demo.travellybe.product.dto.ProductImageDto;
import com.demo.travellybe.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReviewWithProductDto {

    // 상품 정보
    private Long productId;
    private String productName;
    private List<ProductImageDto> images;
    private List<OperationDayDto> operationDays;

    // 리뷰 정보
    private Long reviewId;
    private String reviewerName;
    private LocalDate createdDate;

    public ReviewWithProductDto(Review review) {
        this.productId = review.getProduct().getId();
        this.productName = review.getProduct().getName();
        this.images = review.getProduct().getImages().stream().map(ProductImageDto::new).toList();
        this.operationDays = review.getProduct().getOperationDays().stream().map(OperationDayDto::new).toList();

        this.reviewId = review.getId();
        this.reviewerName = review.getMember().getNickname();
        this.createdDate = review.getCreatedDate().toLocalDate();
    }


}
