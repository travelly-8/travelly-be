package com.demo.travellybe.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    int countReviewsByProductId(@Param("productId") Long productId);
}
