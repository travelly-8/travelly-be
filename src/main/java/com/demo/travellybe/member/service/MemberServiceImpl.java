package com.demo.travellybe.member.service;

import com.demo.travellybe.Reservation.domain.Reservation;
import com.demo.travellybe.Reservation.domain.ReservationStatus;
import com.demo.travellybe.Reservation.repository.ReservationRepository;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.member.dto.ProfileDto;
import com.demo.travellybe.member.dto.TravellerResponseDto;
import com.demo.travellybe.member.dto.TravellyResponseDto;
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

import java.time.LocalTime;
import java.util.List;

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


        int reviewCount = reviews.size() - member.getReviewCount();

        LocalTime now = LocalTime.now();

        int reservationCount = (int) reservations.stream()
                .filter(reservation -> reservation.getStartTime().isAfter(now))
                .count();

        // 최근 본 상품
        List<Long> productIds = productRecentRequestDtoList.stream()
                .map(ProductRecentRequestDto::getProductId)
                .toList();

        List<MyProductResponseDto> recentProducts = productRepository.findByIdIn(productIds).stream()
                .map(MyProductResponseDto::new)
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

        // 답글을 달지 않은 리뷰 개수
        int reviewCount = (int) reviewRepository.countReviewsWithMyComments(member.getId());

        return new TravellyResponseDto(member,reviewCount, reservationCount, products);
    }
}
