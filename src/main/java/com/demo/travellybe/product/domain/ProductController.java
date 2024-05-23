package com.demo.travellybe.product.domain;

import com.demo.travellybe.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "상품 API")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("/")
    public ResponseEntity<Product> getAllProducts() {
        return ResponseEntity.ok((Product) productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        // TODO merge 후 Exception/ErrorCode 사용해서 예외처리
        productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Product>> getProductList(
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
        List<Product> products = productService.getProductList(cityCode, keyword, contentType, sortType, date, startTime, endTime, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }


    @PostMapping("/")
    // TODO ProductDto로 변경
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.ok(product);
    }
}
