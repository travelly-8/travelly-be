package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.OperationDay;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OperationDayDto {
    @NotNull
    @Schema(description = "운영 날짜", examples = "2024-05-29")
    private LocalDate date;

    private List<OperationHourDto> operationDayHours = new ArrayList<>();

    public OperationDayDto(OperationDay operationDay) {
        this.date = operationDay.getDate();
        if (operationDay.getOperationHours() != null) {
            this.operationDayHours = operationDay.getOperationHours().stream().map(OperationHourDto::new).toList();
        }
    }

    @Builder
    public OperationDayDto(LocalDate date, List<OperationHourDto> operationDayHours) {
        this.date = date;
        this.operationDayHours = operationDayHours;
    }
}