package com.demo.travellybe.product.dto.response;

import com.demo.travellybe.product.domain.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketResponseDto {
    @Schema(description = "티켓 ID", example = "1")
    private Long id;

    @Schema(description = "티켓 이름", example = "일일이용권")
    private String name;

    @NotNull
    @Schema(description = "티켓 가격", example = "10000")
    private int price;

    @Schema(description = "티켓 설명", example = "1일 이용 가능한 티켓입니다.")
    private String description;

    public TicketResponseDto(Ticket ticket) {
        this.id = ticket.getId();
        this.name = ticket.getName();
        this.price = ticket.getPrice();
        this.description = ticket.getDescription();
    }
}
