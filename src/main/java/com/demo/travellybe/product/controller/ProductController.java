package com.demo.travellybe.product.controller;

import com.demo.travellybe.product.dto.ProductResponseDto;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import com.demo.travellybe.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "상품 API")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }


    @PostMapping("/")
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody @Valid ProductCreateRequestDto productCreateRequestDto) {
        // TODO JWT 토큰을 통해 사용자 정보를 가져와서 productFormDto.memberId를 설정하는 과정 추가
        productCreateRequestDto.setMemberId(1L);
        ProductResponseDto productResponseDto = productService.addProduct(productCreateRequestDto);
        return ResponseEntity.ok(productResponseDto);
    }

    // TODO 페이징
    @GetMapping("/")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(Pageable pageable) {
        Page<ProductResponseDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    // TODO 페이징
    @GetMapping("/list")
    public ResponseEntity<Page<ProductResponseDto>> getProductList(
            @RequestParam(required = false) String cityCode,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String contentType,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) LocalTime startTime,
            @RequestParam(required = false) LocalTime endTime,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice
    ) {
        Page<ProductResponseDto> products = productService.getProductList(cityCode, keyword, contentType, sortType, date, startTime, endTime, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

}
