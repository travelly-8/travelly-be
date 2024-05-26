package com.demo.travellybe.product.service;


import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.domain.ProductRepository;
import com.demo.travellybe.product.domain.QProduct;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.ProductResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public ProductResponseDto addProduct(ProductCreateRequestDto productCreateRequestDto) {
        Product product = Product.of(productCreateRequestDto);
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
        if (!product.getMember().getId().equals(memberId)) {
                    throw new CustomException(ErrorCode.PRODUCT_NOT_OWNER);
        }
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponseDto::new);
    }

    @Override
    public Page<ProductResponseDto> getProductList(String cityCode, String keyword, String contentType, String sortType, LocalDate date, LocalTime startTime, LocalTime endTime, Integer minPrice, Integer maxPrice) {
        QProduct product = QProduct.product;

        BooleanBuilder builder = new BooleanBuilder();
        if (cityCode != null) {
            builder.and(product.cityCode.eq(cityCode));
        }
        if (keyword != null) {
            builder.and(product.name.containsIgnoreCase(keyword));
        }
        if (contentType != null) {
            builder.and(product.type.eq(contentType));
        }
        // TODO 시간 관련 조건 추가
        if (minPrice != null) {
            builder.and(product.price.goe(minPrice));
        }
        if (maxPrice != null) {
            builder.and(product.price.loe(maxPrice));
        }

        List<Product> fetch = queryFactory.selectFrom(product)
                .where(builder)
                .fetch();

        return null;
    }
}
