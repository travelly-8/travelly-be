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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

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
        Pageable pageable = requestDto.toPageable();
        Page<Product> products = productRepository.getSearchedProducts(requestDto, pageable);

        List<ProductsResponseDto> productDtos = products.stream()
                .map(ProductsResponseDto::new)
                .toList();

        return new PageImpl<>(productDtos, pageable, products.getTotalElements());
    }
}
