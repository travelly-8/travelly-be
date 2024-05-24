package com.demo.travellybe.product.service;

import com.demo.travellybe.product.dto.ProductDto;
import com.demo.travellybe.product.dto.ProductFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ProductService {
    public ProductDto addProduct(ProductFormDto productFormDto);
    public void deleteProduct(Long id);
    public ProductDto updateProduct(Long id, ProductFormDto productFormDto);
    public ProductDto getProductById(Long id);

    public Page<ProductDto> getAllProducts(Pageable pageable);
    public Page<ProductDto> getProductList(String cityCode, String keyword, String contentType, String sortType, LocalDate date, LocalTime startTime, LocalTime endTime, Integer minPrice, Integer maxPrice);
}
