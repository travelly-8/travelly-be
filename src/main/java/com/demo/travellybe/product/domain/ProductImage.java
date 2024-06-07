package com.demo.travellybe.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private int imageOrder;

    public static ProductImage of(String url, int order, Product product) {
        ProductImage productImage = new ProductImage();
        productImage.product = product;
        productImage.url = url;
        productImage.imageOrder = order;
        return productImage;
    }
}
