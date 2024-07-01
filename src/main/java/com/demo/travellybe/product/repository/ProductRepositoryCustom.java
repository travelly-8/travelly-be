package com.demo.travellybe.product.repository;

import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.request.ProductsSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> getSearchedProducts(ProductsSearchRequestDto requestDto, Pageable pageable);
}