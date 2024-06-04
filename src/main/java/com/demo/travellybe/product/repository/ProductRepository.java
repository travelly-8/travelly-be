package com.demo.travellybe.product.repository;

import com.demo.travellybe.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    int countReviewsByProductId(@Param("productId") Long productId);
}
