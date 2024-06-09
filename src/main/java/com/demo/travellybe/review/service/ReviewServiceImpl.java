package com.demo.travellybe.review.service;

import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.repository.ProductRepository;
import com.demo.travellybe.review.domain.Review;
import com.demo.travellybe.review.domain.ReviewRepository;
import com.demo.travellybe.review.dto.ReviewRequestDto;
import com.demo.travellybe.util.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final S3Service s3Service;
    private final ReviewRepository reviewRepository;

    public void saveReview(List<MultipartFile> files, ReviewRequestDto reviewRequestDto, String email, Long productId) {

        // 유저 검색
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 상품 검색
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 파일 저장 및 URL 생성
        List<String> filesUrls = s3Service.uploadFiles(files, "review");

        // 리뷰 저장
        Review review = Review.builder()
                .content(reviewRequestDto.getContent())
                .rating(reviewRequestDto.getRating())
                .imageUrls(filesUrls)
                .build();

        member.addReview(review);
        product.addReview(review);

        reviewRepository.save(review);
    }
}