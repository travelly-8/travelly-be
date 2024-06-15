package com.demo.travellybe.product.dto.response;

import com.demo.travellybe.product.domain.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketResponseDto {
    @Schema(description = "티켓 ID", example = "1")
    private Long id;

    @Schema(description = "티켓 이름", example = "성인")
    private String name;

    @NotNull
    @Schema(description = "티켓 가격", example = "10000")
    private int price;

    public TicketResponseDto(Ticket ticket) {
        this.id = ticket.getId();
        this.name = ticket.getName();
        this.price = ticket.getPrice();
    }
}
