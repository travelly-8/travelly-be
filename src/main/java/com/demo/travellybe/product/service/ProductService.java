package com.demo.travellybe.product.service;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.ProductResponseDto;
import com.demo.travellybe.product.dto.ProductsSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponseDto addProduct(Long memberId, ProductCreateRequestDto productCreateRequestDto);
    void deleteProduct(Long id);
    void updateProduct(Long id, ProductCreateRequestDto productCreateRequestDto);
    ProductResponseDto getProductById(Long id);
    void checkProductOwner(Long productId, Long memberId);
    void checkLogin(PrincipalDetails principalDetails);

    Page<ProductResponseDto> getAllProducts(Pageable pageable);
    Page<ProductResponseDto> getSearchedProducts(ProductsSearchRequestDto productsSearchRequestDto);
}
