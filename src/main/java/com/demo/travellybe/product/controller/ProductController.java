package com.demo.travellybe.product.controller;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Role;
import com.demo.travellybe.product.dto.ProductsSearchRequestDto;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.ProductsRequestDto;
import com.demo.travellybe.product.dto.ProductResponseDto;
import com.demo.travellybe.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    @Operation(summary = "단일 상품 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 상품을 찾을 수 없습니다.")
    })
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }


    @PostMapping("/")
//    @PreAuthorize("hasAnyAuthority('TRAVELLY', 'ADMIN')")
    public ResponseEntity<ProductResponseDto> addProduct(
            @RequestBody @Valid ProductCreateRequestDto productCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ;
        Long memberId = principalDetails.getMember().getId();
        ProductResponseDto productResponseDto = productService.addProduct(memberId, productCreateRequestDto);
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
