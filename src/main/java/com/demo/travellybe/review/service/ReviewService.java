package com.demo.travellybe.review.service;

import com.demo.travellybe.product.dto.response.ProductReviewResponseDto;
import com.demo.travellybe.review.dto.ReviewRequestDto;
import com.demo.travellybe.review.dto.ReviewResponseDto;
import com.demo.travellybe.review.dto.ReviewUpdateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    void saveReview(List<MultipartFile> files, ReviewRequestDto reviewRequestDto, String username, Long productId);

    ReviewResponseDto getReview(Long productId, Long reviewId, String username);

    Page<ProductReviewResponseDto> getProductReviews(Long productId, Pageable pageable);

    ReviewUpdateResponseDto getUpdateReview(Long reviewId);

    void updateReview(List<MultipartFile> files, ReviewRequestDto reviewRequestDto, Long reviewId);

    void deleteReview(Long reviewId);
}
