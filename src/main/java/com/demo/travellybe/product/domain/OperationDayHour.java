package com.demo.travellybe.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OperationDayHour {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_day_hour_id")
    private Long id;

    @Column(nullable = false)
    private String startHour;

    @Column(nullable = false)
    private String endHour;

    @ManyToOne
    @JoinColumn(name = "operation_day_id")
    private OperationDay operationDay;
}