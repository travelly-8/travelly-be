package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class TicketDto {
    @NotNull
    @Schema(description = "티켓 이름", example = "성인")
    private String name;

    @NotNull
    @Schema(description = "티켓 가격", example = "10000")
    private int price;

    @Builder
    public TicketDto(String name, int price, String description) {
        this.name = name;
        this.price = price;
    }

    public TicketDto(Ticket ticket) {
        this.name = ticket.getName();
        this.price = ticket.getPrice();
    }
}
