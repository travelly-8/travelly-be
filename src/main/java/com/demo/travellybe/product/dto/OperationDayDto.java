package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.OperationDay;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OperationDayDto {
    private String date;
    private List<OperationDayHourDto> operationDayHours;

    public OperationDayDto(OperationDay operationDay) {
        this.date = operationDay.getDate();
        this.operationDayHours = operationDay.getOperationDayHours().stream().map(OperationDayHourDto::new).toList();
    }
}