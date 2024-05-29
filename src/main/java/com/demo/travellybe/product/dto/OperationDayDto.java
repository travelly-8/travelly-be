package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.OperationDay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class OperationDayDto {
    @Schema(description = "운영 날짜", examples = "2024-05-29")
    private LocalDate date;
    private List<OperationDayHourDto> operationDayHours;

    public OperationDayDto(OperationDay operationDay) {
        this.date = operationDay.getDate();
        if (operationDay.getOperationDayHours() != null) {
            this.operationDayHours = operationDay.getOperationDayHours().stream().map(OperationDayHourDto::new).toList();
        }
    }

    @Builder
    public OperationDayDto(LocalDate date, List<OperationDayHourDto> operationDayHours) {
        this.date = date;
        this.operationDayHours = operationDayHours;
    }
}