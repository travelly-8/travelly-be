package com.demo.travellybe.product.service;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.product.dto.KeywordRankChangeDto;
import com.demo.travellybe.product.dto.request.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.request.ProductsSearchRequestDto;
import com.demo.travellybe.product.dto.response.MyProductResponseDto;
import com.demo.travellybe.product.dto.response.ProductResponseDto;
import com.demo.travellybe.product.dto.response.ProductWithReservationCountDto;
import com.demo.travellybe.product.dto.response.ProductsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponseDto addProduct(Long memberId, ProductCreateRequestDto productCreateRequestDto);
    void deleteProduct(Long id);
    void updateProduct(Long id, ProductCreateRequestDto productCreateRequestDto);
    ProductResponseDto getProductById(Long id);
    void checkProductOwner(Long productId, Long memberId);
    void checkLogin(PrincipalDetails principalDetails);

    Page<ProductResponseDto> getAllProducts(Pageable pageable);
    Page<ProductsResponseDto> getSearchedProducts(ProductsSearchRequestDto productsSearchRequestDto);

    List<String> getTopSearchKeywords();
    List<KeywordRankChangeDto> getTopSearchKeywordsWithRankChange();
    List<ProductsResponseDto> getTopProducts();

    List<MyProductResponseDto> getMyProducts(String username);

    List<ProductWithReservationCountDto> getReservations(String username);
}
