package com.demo.travellybe.comment.dto;

import com.demo.travellybe.comment.domain.Comment;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.OperationDayDto;
import com.demo.travellybe.product.dto.ProductImageDto;
import com.demo.travellybe.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithProductDto {

    // 상품 정보
    private Long productId;
    private String productName;
    private List<ProductImageDto> images;
    private List<OperationDayDto> operationDays;

    // 후기 정보
    private Long reviewId;

    // 댓글 개수
    private int commentCount;

    public CommentWithProductDto(Review review) {
        this.productId = review.getProduct().getId();
        this.productName = review.getProduct().getName();
        this.images = review.getProduct().getImages().stream().map(ProductImageDto::new).toList();
        this.operationDays = review.getProduct().getOperationDays().stream().map(OperationDayDto::new).toList();
        this.reviewId = review.getId();
        this.commentCount = review.getCommentCount();
    }


}
