package com.demo.travellybe.product.controller;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.member.domain.Role;
import com.demo.travellybe.product.dto.ProductsSearchRequestDto;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.ProductsRequestDto;
import com.demo.travellybe.product.dto.ProductResponseDto;
import com.demo.travellybe.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAnyAuthority('TRAVELLY', 'ADMIN')")
    public ResponseEntity<ProductResponseDto> addProduct(
            @RequestBody @Valid ProductCreateRequestDto productCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        productCreateRequestDto.setMemberId(principalDetails.getMember().getId());
        ProductResponseDto productResponseDto = productService.addProduct(productCreateRequestDto);
        return ResponseEntity.ok(productResponseDto);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody @Valid ProductCreateRequestDto productCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getRole().equals(Role.ADMIN)) {
            productService.checkProductOwner(productId, principalDetails.getMember().getId());
        }
        productService.updateProduct(productId, productCreateRequestDto);
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        return ResponseEntity.ok(productResponseDto);
    }

    // 최신순 평점순 가격순 등등
    @GetMapping("/")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@ModelAttribute ProductsRequestDto productsRequestDto) {
        Pageable pageable = productsRequestDto.toPageable();
        Page<ProductResponseDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ProductResponseDto>> getFilteredProducts(@ModelAttribute ProductsSearchRequestDto requestDto
    ) {
        Page<ProductResponseDto> products = productService.getFilteredProducts(requestDto);
        return ResponseEntity.ok(products);
    }

}
