package com.demo.travellybe.product.service;

import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ProductService {
    ProductResponseDto addProduct(ProductCreateRequestDto productCreateRequestDto);
    void deleteProduct(Long id);
    void updateProduct(Long id, ProductCreateRequestDto productCreateRequestDto);
    ProductResponseDto getProductById(Long id);
    void checkProductOwner(Long productId, Long memberId);

    Page<ProductResponseDto> getAllProducts(Pageable pageable);
    Page<ProductResponseDto> getProductList(String cityCode, String keyword, String contentType, String sortType, LocalDate date, LocalTime startTime, LocalTime endTime, Integer minPrice, Integer maxPrice);
}
