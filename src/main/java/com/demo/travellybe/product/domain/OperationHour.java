package com.demo.travellybe.product.domain;

import com.demo.travellybe.product.dto.OperationDayHourDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class OperationHour {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_hour_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_day_id")
    private OperationDay operationDay;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    public static OperationHour of(OperationDayHourDto operationDayHourDto, OperationDay operationDay) {
        OperationHour operationHour = new OperationHour();
        operationHour.startTime = operationDayHourDto.getStartTime();
        operationHour.endTime = operationDayHourDto.getEndTime();
        operationHour.operationDay = operationDay;
        return operationHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationHour that = (OperationHour) o;
        return Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }
}