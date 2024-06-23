package com.demo.travellybe.review.controller;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.product.dto.response.ProductReviewResponseDto;
import com.demo.travellybe.review.dto.ReviewPageRequestDto;
import com.demo.travellybe.review.dto.ReviewRequestDto;
import com.demo.travellybe.review.dto.ReviewResponseDto;
import com.demo.travellybe.review.dto.ReviewUpdateResponseDto;
import com.demo.travellybe.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/{productId}")
    @Operation(summary = "리뷰 등록")
    public ResponseEntity<Void> saveReview(
            @RequestPart(value="images", required = false) List<MultipartFile> files,
            @RequestPart(value="review") ReviewRequestDto reviewRequestDto,
            @PathVariable("productId") Long productId,
            @AuthenticationPrincipal PrincipalDetails userInfo
    ) {

        reviewService.saveReview(files, reviewRequestDto, userInfo.getUsername(), productId);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{productId}/{reviewId}")
    @Operation(summary = "후기 상세 페이지")
    public ResponseEntity<ReviewResponseDto> getReview(
            @PathVariable(value = "productId") Long productId,
            @PathVariable(value = "reviewId") Long reviewId,
            @AuthenticationPrincipal PrincipalDetails userInfo
    ) {
        return ResponseEntity.ok().body(reviewService.getReview(productId, reviewId, userInfo.getUsername()));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "상품 리뷰 페이지네이션")
    public ResponseEntity<Page<ProductReviewResponseDto>> getProductReviews(
            @PathVariable Long productId,
            @ModelAttribute @Valid ReviewPageRequestDto reviewPageRequestDto
    ) {
        Pageable pageable = reviewPageRequestDto.toPageable();
        return ResponseEntity.ok().body(reviewService.getProductReviews(productId, pageable));
    }

    @GetMapping("/update/{reviewId}")
    @Operation(summary = "리뷰 수정 데이터 반환")
    public ResponseEntity<ReviewUpdateResponseDto> getUpdateReview(@PathVariable("reviewId") Long reviewId) {

        return ResponseEntity.ok().body(reviewService.getUpdateReview(reviewId));
    }

    @PutMapping("/update/{reviewId}")
    @Operation(summary = "상품 리뷰 수정")
    public ResponseEntity<Void> updateReview(
            @RequestPart(value="images", required = false) List<MultipartFile> files,
            @RequestPart(value="review") ReviewRequestDto reviewRequestDto,
            @PathVariable("reviewId") Long reviewId
    ) {

        reviewService.updateReview(files, reviewRequestDto, reviewId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{reviewId}")
    @Operation(summary = "상품 리뷰 삭제")
    public ResponseEntity<Void> deleteReview(@PathVariable("reviewId") Long reviewId) {

        reviewService.deleteReview(reviewId);

        return ResponseEntity.ok().build();
    }
}
