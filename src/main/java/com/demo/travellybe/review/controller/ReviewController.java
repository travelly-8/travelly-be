package com.demo.travellybe.review.controller;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.review.dto.ReviewRequestDto;
import com.demo.travellybe.review.dto.ReviewResponseDto;
import com.demo.travellybe.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "리뷰 등록")
    public ResponseEntity<Long> saveReview(
            @RequestPart(value="images", required = false) List<MultipartFile> files,
            @RequestPart(value="review") ReviewRequestDto reviewRequestDto,
            @PathVariable Long productId,
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


}
