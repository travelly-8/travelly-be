package com.demo.travellybe.product.domain;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationDay> operationDays = new ArrayList<>();
}
