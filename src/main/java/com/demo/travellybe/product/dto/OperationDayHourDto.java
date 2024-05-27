package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.OperationDayHour;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter @Setter
public class OperationDayHourDto {
    private LocalTime startTime;
    private LocalTime endTime;

    public OperationDayHourDto(OperationDayHour operationDayHour) {
        this.startTime = operationDayHour.getStartTime();
        this.endTime = operationDayHour.getEndTime();
    }
}
