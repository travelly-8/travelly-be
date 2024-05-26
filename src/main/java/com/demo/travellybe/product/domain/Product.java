package com.demo.travellybe.product.domain;

import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
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

    public static Product of(ProductCreateRequestDto productCreateRequestDto) {
        Product product = new Product();
        product.name = productCreateRequestDto.getName();
        product.price = productCreateRequestDto.getPrice();
        product.type = productCreateRequestDto.getType();
        product.description = productCreateRequestDto.getDescription();
        product.imageUrl = productCreateRequestDto.getImageUrl();
        product.address = productCreateRequestDto.getAddress();
        product.detailAddress = productCreateRequestDto.getDetailAddress();
        product.phoneNumber = productCreateRequestDto.getPhoneNumber();
        product.homepage = productCreateRequestDto.getHomepage();
        product.cityCode = productCreateRequestDto.getCityCode();
        product.ticketCount = productCreateRequestDto.getTicketCount();
        product.ticketPrice = productCreateRequestDto.getTicketPrice();
        product.rating = 0.0;
        product.enabled = true;

        product.operationDays = productCreateRequestDto.getOperationDays().stream().map(operationDayDto ->
                OperationDay.of(operationDayDto, product)).toList();
        return product;
    }

    // TODO: update 메서드 구현
    public void update(ProductCreateRequestDto productCreateRequestDto) {
    }
}
