package com.demo.travellybe.product.controller;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.exception.ErrorResponse;
import com.demo.travellybe.member.domain.Role;
import com.demo.travellybe.product.dto.request.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.response.ProductResponseDto;
import com.demo.travellybe.product.dto.response.ProductsResponseDto;
import com.demo.travellybe.product.dto.request.ProductsSearchRequestDto;
import com.demo.travellybe.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "상품 API")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    @Operation(summary = "상품 상세 조회",
            description = "상품의 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "404", description = "해당 상품을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<ProductResponseDto> getProductById(
            @Parameter(description = "조회할 상품 ID", example = "1")
            @PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제",
            description = "상품을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 상품의 소유자가 아닙니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 상품을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "삭제할 상품 ID", example = "1")
            @PathVariable Long productId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        productService.checkLogin(principalDetails);
        if (!principalDetails.getMember().getRole().equals(Role.ADMIN)) {
            productService.checkProductOwner(productId, principalDetails.getMember().getId());
        }
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/")
//    @PreAuthorize("hasAnyAuthority('TRAVELLY', 'ADMIN')")
    @Operation(summary = "상품 등록",
            description = "상품을 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ProductCreateRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "상품을 등록하려는 회원을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<ProductResponseDto> addProduct(
            @RequestBody @Valid ProductCreateRequestDto productCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        productService.checkLogin(principalDetails);
        Long memberId = principalDetails.getMember().getId();
        ProductResponseDto productResponseDto = productService.addProduct(memberId, productCreateRequestDto);
        return ResponseEntity.ok(productResponseDto);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정",
            description = "상품을 수정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ProductCreateRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "해당 상품의 소유자가 아닙니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 상품을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<ProductResponseDto> updateProduct(
            @Parameter(description = "수정할 상품 ID", example = "1")
            @PathVariable Long productId,
            @RequestBody @Valid ProductCreateRequestDto productCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        if (!principalDetails.getMember().getRole().equals(Role.ADMIN)) {
            productService.checkProductOwner(productId, principalDetails.getMember().getId());
        }
        productService.updateProduct(productId, productCreateRequestDto);
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        return ResponseEntity.ok(productResponseDto);
    }

    @GetMapping("/")
    @Operation(summary = "상품 목록 조회",
            description = "페이징 처리된 상품 목록을 조회합니다.\n" +
                    "page, size 필수",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공")
            })
    public ResponseEntity<Page<ProductsResponseDto>> getAllProducts(ProductsSearchRequestDto searchDto) {
        Page<ProductsResponseDto> products = productService.getSearchedProducts(searchDto);
        return ResponseEntity.ok(products);
    }

}
