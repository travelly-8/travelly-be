package com.demo.travellybe.product.repository;

import com.demo.travellybe.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    int countReviewsByProductId(@Param("productId") Long productId);
    @Query("SELECT p.id FROM Product p WHERE p.member.id = :memberId")
    List<Long> findProductIdsByMemberId(@Param("memberId") Long memberId);
    List<Product> findByMemberId(Long memberId);

    List<Product> findByIdIn(List<Long> productIds);
    List<Product> findAllByMemberId(Long memberId);
}
