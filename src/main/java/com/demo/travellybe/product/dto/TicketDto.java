package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class TicketDto {
    @NotNull
    @Schema(description = "티켓 이름", example = "일일이용권")
    private String name;

    @NotNull
    @Schema(description = "티켓 가격", example = "10000")
    private int price;

    @NotNull
    @Schema(description = "티켓 설명", example = "1일 이용 가능한 티켓입니다.")
    private String description;

    @Builder
    public TicketDto(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public TicketDto(Ticket ticket) {
        this.name = ticket.getName();
        this.price = ticket.getPrice();
        this.description = ticket.getDescription();
    }
}
