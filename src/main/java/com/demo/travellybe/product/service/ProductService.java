package com.demo.travellybe.product.service;

import com.demo.travellybe.product.domain.Product;

import java.util.List;

public interface ProductService {
    public void addProduct(Product product);
    public void deleteProduct(Product product);
    public void updateProduct(Product product);
    public Product getProductById(Long id);
    public List<Product> getAllProducts();
}
