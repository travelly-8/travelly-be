package com.demo.travellybe.product.service;

import com.demo.travellybe.product.domain.Product;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ProductService {
    public void addProduct(Product product);
    public void deleteProduct(Long id);
    public void updateProduct(Product product);
    public Product getProductById(Long id);

    // TODO DTO로 변경
    public List<Product> getAllProducts();
    List<Product> getProductList(String cityCode, String keyword, String contentType, String sortType, LocalDate date, LocalTime startTime, LocalTime endTime, Integer minPrice, Integer maxPrice);
}
