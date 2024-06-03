package com.demo.travellybe.product.service;


import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.domain.ProductRepository;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.ProductResponseDto;
import com.demo.travellybe.product.dto.ProductsSearchRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.demo.travellybe.product.domain.QProduct.product;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public ProductResponseDto addProduct(Long memberId, ProductCreateRequestDto productCreateRequestDto) {
        Product product = Product.of(productCreateRequestDto);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        product.setMember(member);
        productRepository.save(product);
        return new ProductResponseDto(product);
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
        return new ProductResponseDto(product);
    }

    @Override
    public void checkProductOwner(Long productId, Long memberId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        Member member = product.getMember();
        // 상품 소유자가 아니거나 관리자가 아닌 경우 예외 발생
//        if (!(member.getId().equals(memberId) || member.getRole().equals(Role.ADMIN)))
//            throw new CustomException(ErrorCode.PRODUCT_NOT_OWNER);
        if (!(member.getId().equals(memberId)))
            throw new CustomException(ErrorCode.PRODUCT_NOT_OWNER);
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponseDto> productDtos = products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
        return new PageImpl<>(productDtos, pageable, products.getTotalElements());
    }


    @Override
    public Page<ProductResponseDto> getFilteredProducts(ProductsSearchRequestDto requestDto) {
        Pageable pageable = requestDto.toPageable();

        QueryResults<Product> results = queryFactory
                .selectFrom(product)
                .where(eqCityCode(requestDto.getCityCode()),
                        eqContentType(requestDto.getContentType()),
                        containsKeyword(requestDto.getKeyword()),
                        betweenPrice(requestDto.getMinPrice(), requestDto.getMaxPrice()),
                        betweenDate(requestDto.getStartDate(), requestDto.getEndDate()),
                        betweenTime(requestDto.getStartTime(), requestDto.getEndTime()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ProductResponseDto> productDtos = results.getResults().stream()
                .map(ProductResponseDto::new)
                .toList();

        return new PageImpl<>(productDtos, pageable, results.getTotal());
    }

    private BooleanExpression eqCityCode(String cityCode) {
        if (!StringUtils.hasText(cityCode)) return null;
        return product.cityCode.eq(cityCode);
    }

    private BooleanExpression eqContentType(String contentType) {
        if (!StringUtils.hasText(contentType)) return null;
        return product.type.eq(contentType);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) return null;
        return product.name.contains(keyword)
                .or(product.description.contains(keyword));
    }

    private BooleanExpression betweenPrice(Integer minPrice, Integer maxPrice) {
        if (minPrice == null || maxPrice == null) return null;
        return product.minPrice.goe(minPrice)
                .and(product.maxPrice.loe(maxPrice));
    }

    private BooleanExpression betweenDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return null;
        return product.operationDays.any().date.between(startDate, endDate);
    }

    private BooleanExpression betweenTime(String startTime, String endTime) {
        if (startTime == null || endTime == null) return null;
        LocalTime st = LocalTime.parse(startTime);
        LocalTime et = LocalTime.parse(endTime);
        return product.operationDays.any().operationDayHours.any().startTime.goe(st)
                .and(product.operationDays.any().operationDayHours.any().endTime.loe(et));
    }
}
