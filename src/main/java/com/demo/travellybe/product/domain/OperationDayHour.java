package com.demo.travellybe.product.domain;

import com.demo.travellybe.product.dto.OperationDayHourDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_day_id")
    private OperationDay operationDay;

    public static OperationDayHour of(OperationDayHourDto operationDayHourDto, OperationDay operationDay) {
        OperationDayHour operationDayHour = new OperationDayHour();
        operationDayHour.startHour = operationDayHourDto.getStartHour();
        operationDayHour.endHour = operationDayHourDto.getEndHour();
        operationDayHour.operationDay = operationDay;
        return operationDayHour;
    }
}