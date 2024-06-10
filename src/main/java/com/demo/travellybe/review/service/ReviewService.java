package com.demo.travellybe.review.service;

import com.demo.travellybe.review.dto.ReviewRequestDto;
import com.demo.travellybe.review.dto.ReviewResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    void saveReview(List<MultipartFile> files, ReviewRequestDto reviewRequestDto, String username, Long productId);

    ReviewResponseDto getReview(Long productId, Long reviewId, String username);
}
