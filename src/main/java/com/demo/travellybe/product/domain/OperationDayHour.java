package com.demo.travellybe.product.domain;

import com.demo.travellybe.product.dto.OperationDayHourDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
public class OperationDayHour {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_day_hour_id")
    private Long id;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_day_id")
    private OperationDay operationDay;

    public static OperationDayHour of(OperationDayHourDto operationDayHourDto, OperationDay operationDay) {
        OperationDayHour operationDayHour = new OperationDayHour();
        operationDayHour.startTime = operationDayHourDto.getStartTime();
        operationDayHour.endTime = operationDayHourDto.getEndTime();
        operationDayHour.operationDay = operationDay;
        return operationDayHour;
    }
}