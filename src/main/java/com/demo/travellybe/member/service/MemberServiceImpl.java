package com.demo.travellybe.member.service;

import com.demo.travellybe.Reservation.domain.Reservation;
import com.demo.travellybe.Reservation.domain.ReservationStatus;
import com.demo.travellybe.Reservation.repository.ReservationRepository;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.member.dto.*;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.request.ProductRecentRequestDto;
import com.demo.travellybe.product.dto.response.MyProductResponseDto;
import com.demo.travellybe.product.repository.ProductRepository;
import com.demo.travellybe.review.domain.Review;
import com.demo.travellybe.review.domain.ReviewRepository;
import com.demo.travellybe.util.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3Service s3Service;

    public ProfileDto getProfile(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return ProfileDto.of(member);
    }

    public ProfileDto updateNickname(String email, String nickname) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.setNickname(nickname);
        return ProfileDto.of(member);
    }

    public ProfileDto updateImage(String email, MultipartFile file) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.setImageUrl(s3Service.uploadFile(file, "profile"));
        return ProfileDto.of(member);
    }

    public ProfileDto updateDefaultImage(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
;
        member.setImageUrl("https://travelly-bucket.s3.ap-northeast-2.amazonaws.com/images/profile/default-profile.png");
        return ProfileDto.of(member);
    }


    public ProfileDto updatePassword(String email, String password, String newPassword) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));


        // 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.MEMBER_PASSWORD_MISMATCH);
        }

        member.setPassword(bCryptPasswordEncoder.encode(newPassword));
        return ProfileDto.of(member);
    }

    public TravellerResponseDto getTravellerData(List<ProductRecentRequestDto> productRecentRequestDtoList, String email) {

        // 유저 검색
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 리뷰 검색
        List<Review> reviews = reviewRepository.findByMemberId(member.getId());

        // 예약 검색
        List<Reservation> reservations = reservationRepository.findByBuyerId(member.getId());

        LocalDate now = LocalDate.now();

        int reservationCount = (int) reservations.stream()
                .filter(reservation -> reservation.getDate().isAfter(now))
                .count();

        // 작성하지 않은 리뷰의 개수 = 예약 수 - 리뷰 수
        // 리뷰 생성 조건을 엄격하게 하지 않아서 리뷰 수가 예약 수보다 많을 수 있음 -> 음수가 나올 수 있음
        int reviewCount = (int) reviewRepository.countByMemberId(member.getId());

        // 최근 본 상품
        List<Long> productIds = productRecentRequestDtoList.stream()
                .map(ProductRecentRequestDto::getProductId)
                .toList();

        List<MyProductResponseDto> recentProducts = productRepository.findByIdIn(productIds).stream()
                .map(product -> new MyProductResponseDto(product, (int) reviewRepository.countByProductId(product.getId())))
                .toList();

        return new TravellerResponseDto(member, reviewCount, reservationCount, recentProducts);
    }

    public TravellyResponseDto getTravellyData(String email) {

        // 유저 검색
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 내 상품 검색
        List<Product> products = productRepository.findByMemberId(member.getId());

        // 새로 들어온 예약 검색
        int reservationCount = (int) products.stream()
                .map(product -> reservationRepository.findByProductId(product.getId()))
                .flatMap(List::stream)
                .filter(reservation -> reservation.getStatus().equals(ReservationStatus.PENDING))
                .count();

        // 답글을 달지 않은 리뷰 개수 = 상품에 달린 리뷰 수 - 내가 답글을 단 리뷰 수
        int productReviewCount = (int) reviewRepository.countByProductId(member.getId());
        int reviewsWithMyCommentsCount = (int) reviewRepository.countReviewsWithMyComments(member.getId());
        int notResponseReviewCount = Math.max(0, productReviewCount - reviewsWithMyCommentsCount);

        // 상품별 리뷰 개수 계산
        List<MyProductResponseDto> productDtos = products.stream()
                .map(product -> new MyProductResponseDto(product, (int) reviewRepository.countByProductId(product.getId())))
                .collect(Collectors.toList());

        return new TravellyResponseDto(member, notResponseReviewCount, reservationCount, productDtos);
    }

    public TravellyReviewResponseDto getTravellyReview(String email) {

        // 유저 검색
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 특정 회원의 productId 리스트
        List<Long> productIds = productRepository.findProductIdsByMemberId(member.getId());

        // 최신순으로 받은 리뷰 리스트
        List<Review> reviews = reviewRepository.findRecentReviewsByProductIds(productIds);

        // 댓글을 작성한 리뷰
        List<Review> commentReviews = reviewRepository.findReviewsCommentedByMember(member.getId());

        return new TravellyReviewResponseDto(member, reviews, commentReviews);
    }

    public TravellerReviewResponseDto getTravellerReview(String email) {

        // 유저 검색
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 유저가 체험한 상품 리스트
        List<Reservation> reservations = reservationRepository.findByBuyerId(member.getId());

        List<Product> products = reservations.stream()
                .map(reservation -> reservationRepository.findAcceptedProductByReservationId(reservation.getId()))
                .filter(Objects::nonNull)
                .toList();

        // 유저가 작성한 리뷰
        List<Review> reviews = reviewRepository.findByMemberId(member.getId());

        return new TravellerReviewResponseDto(member, products, reviews);

    }
}
