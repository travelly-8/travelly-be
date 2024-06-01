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
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        BooleanBuilder builder = buildQuery(requestDto);
        Pageable pageable = requestDto.toPageable();

        QueryResults<Product> results = queryFactory.selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ProductResponseDto> productDtos = results.getResults().stream()
                .map(ProductResponseDto::new)
                .toList();

        return new PageImpl<>(productDtos, pageable, results.getTotal());
    }

    private BooleanBuilder buildQuery(ProductsSearchRequestDto requestDto) {
        BooleanBuilder builder = new BooleanBuilder();

        Optional.ofNullable(requestDto.getKeyword()).ifPresent(keyword ->
                builder.and(product.name.contains(keyword)
                        .or(product.description.contains(keyword))));

        Optional.ofNullable(requestDto.getCityCode()).ifPresent(cityCode ->
                builder.and(product.cityCode.eq(cityCode)));

        Optional.ofNullable(requestDto.getContentType()).ifPresent(contentType ->
                builder.and(product.type.eq(contentType)));

        Optional.ofNullable(requestDto.getMinPrice()).ifPresent(minPrice ->
                builder.and(product.minPrice.goe(minPrice)));

        Optional.ofNullable(requestDto.getMaxPrice()).ifPresent(maxPrice ->
                builder.and(product.maxPrice.loe(maxPrice)));

        if (requestDto.getStartDate() != null && requestDto.getEndDate() != null) {
            builder.and(product.operationDays.any().date.between(requestDto.getStartDate(), requestDto.getEndDate()));
        }

        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null) {
            LocalTime startTime = LocalTime.parse(requestDto.getStartTime());
            LocalTime endTime = LocalTime.parse(requestDto.getEndTime());
            // startTime, endTime 사이의 범위가 opeartionDayHour의 startTime, endTime 사이의 범위에 포함되는 경우
            builder.and(product.operationDays.any().operationDayHours.any().startTime.goe(startTime)
                    .and(product.operationDays.any().operationDayHours.any().endTime.loe(endTime)));
        }


        return builder;
    }
}
