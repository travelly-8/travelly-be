package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.OperationDayHour;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OperationDayHourDto {
    private String startHour;
    private String endHour;

    public OperationDayHourDto(OperationDayHour operationDayHour) {
        this.startHour = operationDayHour.getStartHour();
        this.endHour = operationDayHour.getEndHour();
    }
}
