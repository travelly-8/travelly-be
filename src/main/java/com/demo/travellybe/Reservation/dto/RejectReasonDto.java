package com.demo.travellybe.Reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RejectReasonDto {
    @Schema(description = "거절 사유", example = "예약 거절 시 입력받을 거절 사유")
    private String rejectReason;
}
