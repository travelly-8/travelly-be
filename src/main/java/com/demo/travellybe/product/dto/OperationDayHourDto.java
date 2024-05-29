package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.OperationDayHour;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter @Setter
public class OperationDayHourDto {
    @Schema(description = "시작 시간", example = "09:00")
    private LocalTime startTime;

    @Schema(description = "종료 시간", example = "12:00")
    private LocalTime endTime;

    public OperationDayHourDto(OperationDayHour operationDayHour) {
        this.startTime = operationDayHour.getStartTime();
        this.endTime = operationDayHour.getEndTime();
    }

    @Builder
    public OperationDayHourDto(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
