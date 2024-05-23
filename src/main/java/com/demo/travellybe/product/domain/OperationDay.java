package com.demo.travellybe.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class OperationDay {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_day_id")
    private Long id;

    @Column(nullable = false)
    private String date;

    @OneToMany(mappedBy = "operationDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationDayHour> operationDayHours = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}