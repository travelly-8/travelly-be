package com.demo.travellybe.product.service;

import com.demo.travellybe.product.dto.ProductResponseDto;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ProductService {
    public ProductResponseDto addProduct(ProductCreateRequestDto productCreateRequestDto);
    public void deleteProduct(Long id);
    public ProductResponseDto updateProduct(Long id, ProductCreateRequestDto productCreateRequestDto);
    public ProductResponseDto getProductById(Long id);

    public Page<ProductResponseDto> getAllProducts(Pageable pageable);
    public Page<ProductResponseDto> getProductList(String cityCode, String keyword, String contentType, String sortType, LocalDate date, LocalTime startTime, LocalTime endTime, Integer minPrice, Integer maxPrice);
}
