package com.demo.travellybe.product.domain;

import com.demo.travellybe.product.dto.OperationDayDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate date;

    @OneToMany(mappedBy = "operationDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationDayHour> operationDayHours = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public static OperationDay of(OperationDayDto operationDayDto, Product product) {
        OperationDay operationDay = new OperationDay();
        operationDay.date = operationDayDto.getDate();
        operationDay.product = product;
        operationDay.operationDayHours = operationDayDto.getOperationDayHours().stream().map(operationDayHourDto ->
                OperationDayHour.of(operationDayHourDto, operationDay)).toList();
        return operationDay;
    }
}