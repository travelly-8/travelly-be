package com.demo.travellybe.product.service;


import com.demo.travellybe.Reservation.domain.ReservationStatus;
import com.demo.travellybe.Reservation.repository.ReservationRepository;
import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.repository.MemberRepository;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.KeywordRankChangeDto;
import com.demo.travellybe.product.dto.request.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.request.ProductsSearchRequestDto;
import com.demo.travellybe.product.dto.response.MyProductResponseDto;
import com.demo.travellybe.product.dto.response.ProductResponseDto;
import com.demo.travellybe.product.dto.response.ProductWithReservationCountDto;
import com.demo.travellybe.product.dto.response.ProductsResponseDto;
import com.demo.travellybe.product.repository.ProductRepository;
import com.demo.travellybe.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public ProductResponseDto addProduct(Long memberId, ProductCreateRequestDto productCreateRequestDto) {
        Product product = Product.of(productCreateRequestDto);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        product.setMember(member);
        productRepository.save(product);
        return new ProductResponseDto(product, 0);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.deleteById(id);
    }

    @Override
    public void updateProduct(Long id, ProductCreateRequestDto productCreateRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        product.update(productCreateRequestDto);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        // 상품 id를 Redis에 저장
        redisTemplate.opsForZSet().incrementScore("popular_products", String.valueOf(id), 1);
        int reviewCount = (int) reviewRepository.countByProductId(id);
        return new ProductResponseDto(product, reviewCount);
    }

    @Override
    public void checkProductOwner(Long productId, Long memberId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        Member member = product.getMember();
        if (!(member.getId().equals(memberId)))
            throw new CustomException(ErrorCode.PRODUCT_NOT_OWNER);
    }

    @Override
    public void checkLogin(PrincipalDetails principalDetails) {
        if (principalDetails == null) throw new CustomException(ErrorCode.LOGIN_REQUIRED);
    }


    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponseDto> productDtos = products.stream()
                .map(product -> new ProductResponseDto(product, (int) reviewRepository.countByProductId(product.getId())))
                .collect(Collectors.toList());
        return new PageImpl<>(productDtos, pageable, products.getTotalElements());
    }


    @Override
    public Page<ProductsResponseDto> getSearchedProducts(ProductsSearchRequestDto requestDto) {
        // 검색 키워드를 Redis에 저장
        if (requestDto.getKeyword() != null && !requestDto.getKeyword().isEmpty())
            redisTemplate.opsForZSet().incrementScore("popular_keywords", requestDto.getKeyword(), 1);

        Pageable pageable = requestDto.toPageable();
        Page<Product> products = productRepository.getSearchedProducts(requestDto, pageable);

        List<ProductsResponseDto> productDtos = products.stream()
                .map(product -> new ProductsResponseDto(product, (int) reviewRepository.countByProductId(product.getId())))
                .toList();

        return new PageImpl<>(productDtos, pageable, products.getTotalElements());
    }

    @Override
    public List<String> getTopSearchKeywords() {
        // Redis에서 인기검색어 top 10을 조회하여 반환
        Set<String> topKeywords = redisTemplate.opsForZSet().reverseRange("popular_keywords", 0, 9);
        if (topKeywords == null) return new ArrayList<>();
        return new ArrayList<>(topKeywords);
    }

    @Override
    public List<KeywordRankChangeDto> getTopSearchKeywordsWithRankChange() {
        // Redis에서 인기검색어 top 10을 조회
        Set<String> topKeywords = redisTemplate.opsForZSet().reverseRange("popular_keywords", 0, 9);
        List<KeywordRankChangeDto> keywordRankChangeDtoList = new ArrayList<>();

        if (topKeywords == null) return keywordRankChangeDtoList;

        int rank = 1;
        for (String keyword : topKeywords) {
            // 현재 랭킹
            int currentRank = rank++;

            // 이전 랭킹 조회
            Long previousRankLong = redisTemplate.opsForZSet().reverseRank("popular_keywords_prev", keyword);
            int previousRank = previousRankLong != null ? previousRankLong.intValue() + 1 : currentRank;

            // 랭킹 변화 계산
            int rankChange = previousRank - currentRank;

            // KeywordRankChangeDto 생성 및 리스트에 추가
            keywordRankChangeDtoList.add(new KeywordRankChangeDto(keyword, currentRank, previousRank, rankChange));
        }

        // 현재 인기검색어를 이전 인기검색어로 복사
        redisTemplate.opsForZSet().intersectAndStore("popular_keywords", "popular_keywords", "popular_keywords_prev");

        return keywordRankChangeDtoList;
    }


    @Override
    public List<ProductsResponseDto> getTopProducts() {
        // Redis에서 인기상품 top 10을 조회하여 반환
        Set<String> topProducts = redisTemplate.opsForZSet().reverseRange("popular_products", 0, 19);
        if (topProducts == null) return new ArrayList<>();

        // 모든 상품을 조회하고 Map에 저장
        Map<Long, Product> productMap = productRepository.findAllById(topProducts.stream()
                        .map(Long::parseLong).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // 원래 순서를 유지하면서 ProductsResponseDto 리스트를 생성
        return topProducts.stream()
                .map(Long::parseLong)
                .filter(productId -> {
                    Product product = productMap.get(productId);
                    if (product == null) {
                        // 상품이 삭제되었으면 Redis에서 해당 상품 ID를 삭제
                        redisTemplate.opsForZSet().remove("popular_products", String.valueOf(productId));
                        return false;
                    }
                    return true;
                })
                .map(productMap::get)
                .map(product -> new ProductsResponseDto(product, (int) reviewRepository.countByProductId(product.getId())))
                .limit(10)
                .collect(Collectors.toList());
    }


    @Override
    public List<MyProductResponseDto> getMyProducts(String email) {

        // 유저 검색
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 유저가 가진 상품 검색
        List<Product> products = productRepository.findAllByMemberId(member.getId());


        return products.stream()
                .map(product -> new MyProductResponseDto(product, (int) reviewRepository.countByProductId(product.getId())))
                .toList();
    }

    public List<ProductWithReservationCountDto> getReservations(String email) {

        // 유저 검색
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 유저가 가진 상품 검색
        List<Product> products = productRepository.findByMemberId(member.getId());

        return products.stream()
                .map(product -> {

                    // 상품 별 새로운 예약 수
                    long reviewCount = reservationRepository.findByProductId(product.getId()).stream()
                            .filter(reservation -> reservation.getStatus().equals(ReservationStatus.PENDING))
                            .count();

                    return new ProductWithReservationCountDto(product, (int) reviewCount);
                })
                .toList();
    }

}
