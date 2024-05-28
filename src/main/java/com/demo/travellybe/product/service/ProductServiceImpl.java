package com.demo.travellybe.product.service;


import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.member.domain.Role;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.domain.ProductRepository;
import com.demo.travellybe.product.domain.QProduct;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.ProductResponseDto;
import com.demo.travellybe.product.dto.ProductsSearchRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
    public ProductResponseDto addProduct(ProductCreateRequestDto productCreateRequestDto) {
        Product product = Product.of(productCreateRequestDto);
        Member member = memberRepository.findById(productCreateRequestDto.getMemberId())
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
        if (!(member.getId().equals(memberId) || member.getRole().equals(Role.ADMIN)))
            throw new CustomException(ErrorCode.PRODUCT_NOT_OWNER);
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponseDto::new);
    }

    // TODO 성능최적화 필요
    @Override
    public Page<ProductResponseDto> getFilteredProducts(ProductsSearchRequestDto requestDto) {
        Pageable pageRequest = PageRequest.of(requestDto.getPage(), requestDto.getSize());

        BooleanBuilder builder = new BooleanBuilder();
        if (requestDto.getCityCode() != null) {
            builder.and(product.cityCode.eq(requestDto.getCityCode()));
        }
        if (requestDto.getKeyword() != null) {
            builder.and(product.name.containsIgnoreCase(requestDto.getKeyword()));
        }
        if (requestDto.getContentType() != null) {
            builder.and(product.type.eq(requestDto.getContentType()));
        }
        if (requestDto.getDate() != null) {
            builder.and(product.operationDays.any().date.eq(requestDto.getDate()));
        }
        if (requestDto.getStartTime() != null) {
            builder.and(product.operationDays.any().operationDayHours.any().startTime.goe(requestDto.getStartTime()));
        }
        if (requestDto.getEndTime() != null) {
            builder.and(product.operationDays.any().operationDayHours.any().endTime.loe(requestDto.getEndTime()));
        }
        if (requestDto.getMinPrice() != null) {
            builder.and(product.price.goe(requestDto.getMinPrice()));
        }
        if (requestDto.getMaxPrice() != null) {
            builder.and(product.price.loe(requestDto.getMaxPrice()));
        }

        List<Product> fetch = queryFactory.selectFrom(product)
                .where(builder)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();


        List<ProductResponseDto> responseDtos = fetch.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(responseDtos, pageRequest, fetch.size());
    }
}
