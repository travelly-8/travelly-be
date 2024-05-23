package com.demo.travellybe.product.controller;

import com.demo.travellybe.product.dto.ProductDto;
import com.demo.travellybe.product.dto.ProductFormDto;
import com.demo.travellybe.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }


    @PostMapping("/")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductFormDto productFormDto) {
        // TODO JWT 토큰을 통해 사용자 정보를 가져와서 productFormDto.memberId를 설정하는 과정 추가
        productFormDto.setMemberId(1L);
        ProductDto productDto = productService.addProduct(productFormDto);
        return ResponseEntity.ok(productDto);
    }

    // TODO 페이징
    @GetMapping("/")
    public ResponseEntity<Page<ProductDto>> getAllProducts(Pageable pageable) {
        Page<ProductDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    // TODO 페이징
    @GetMapping("/list")
    public ResponseEntity<Page<ProductDto>> getProductList(
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
        Page<ProductDto> products = productService.getProductList(cityCode, keyword, contentType, sortType, date, startTime, endTime, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

}
