package com.demo.travellybe.product.domain;

import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.product.dto.ProductFormDto;
import com.demo.travellybe.review.domain.Review;
import com.demo.travellybe.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Product extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String type;

    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private String address;

    private String detailAddress;

    @Column(nullable = false)
    private String phoneNumber;

    private String homepage;

    @Column(nullable = false)
    private String cityCode;

    @Column(nullable = false)
    private int ticketCount;

    @Embedded
    private TicketPrice ticketPrice;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationDay> operationDays = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public static Product of(ProductFormDto productFormDto) {
        Product product = new Product();
        product.name = productFormDto.getName();
        product.price = productFormDto.getPrice();
        product.type = productFormDto.getType();
        product.description = productFormDto.getDescription();
        product.imageUrl = productFormDto.getImageUrl();
        product.address = productFormDto.getAddress();
        product.detailAddress = productFormDto.getDetailAddress();
        product.phoneNumber = productFormDto.getPhoneNumber();
        product.homepage = productFormDto.getHomepage();
        product.cityCode = productFormDto.getCityCode();
        product.ticketCount = productFormDto.getTicketCount();
        product.ticketPrice = productFormDto.getTicketPrice();
        product.rating = 0.0;
        product.enabled = true;

        product.operationDays = productFormDto.getOperationDays().stream().map(operationDayDto ->
                OperationDay.of(operationDayDto, product)).toList();
        return product;
    }

    // TODO: update 메서드 구현
    public void update(ProductFormDto productFormDto) {
    }
}
