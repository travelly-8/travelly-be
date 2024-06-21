package com.demo.travellybe.member.dto;

import com.demo.travellybe.product.dto.OperationDayDto;
import com.demo.travellybe.product.dto.ProductImageDto;
import com.demo.travellybe.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TravellyCommentResponseDto {

    // 상품 정보
    private Long productId;
    private String productName;
    private int productPrice;
    private List<ProductImageDto> images;
    private List<OperationDayDto> operationDays;

    private Long reviewId;


    public TravellyCommentResponseDto(Review review) {
        this.productId = review.getProduct().getId();
        this.productName = review.getProduct().getName();
        this.productPrice = review.getProduct().getMaxPrice();
        this.images = review.getProduct().getImages().stream().map(ProductImageDto::new).toList();
        this.operationDays = review.getProduct().getOperationDays().stream().map(OperationDayDto::new).toList();
        this.reviewId = review.getId();
    }


}
