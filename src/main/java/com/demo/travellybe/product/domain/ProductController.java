package com.demo.travellybe.product.domain;

import com.demo.travellybe.product.service.ProductService;
import com.demo.travellybe.product.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
//@Tag(name = "Product", description = "상품 API")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    // 전체 상품 조회
    @GetMapping("/")
    public ResponseEntity<Product> getAllProducts() {
        return ResponseEntity.ok((Product) productService.getAllProducts());
    }

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    // 상품 등록
    @PostMapping("/")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.ok(product);
    }
}
