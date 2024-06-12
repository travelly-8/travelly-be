package com.demo.travellybe.product.service;


import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.request.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.response.ProductResponseDto;
import com.demo.travellybe.product.dto.response.ProductsResponseDto;
import com.demo.travellybe.product.dto.request.ProductsSearchRequestDto;
import com.demo.travellybe.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

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
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
        return new PageImpl<>(productDtos, pageable, products.getTotalElements());
    }


    @Override
    public Page<ProductsResponseDto> getSearchedProducts(ProductsSearchRequestDto requestDto) {
        // 검색 키워드를 Redis에 저장
        redisTemplate.opsForZSet().incrementScore("popular_keywords", requestDto.getKeyword(), 1);

        Pageable pageable = requestDto.toPageable();
        Page<Product> products = productRepository.getSearchedProducts(requestDto, pageable);

        List<ProductsResponseDto> productDtos = products.stream()
                .map(ProductsResponseDto::new)
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
}
