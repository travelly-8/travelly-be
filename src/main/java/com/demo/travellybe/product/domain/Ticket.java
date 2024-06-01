package com.demo.travellybe.product.domain;

import com.demo.travellybe.product.dto.TicketDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String description;

    public static Ticket of(TicketDto ticketDto, Product product) {
        Ticket ticket = new Ticket();
        ticket.product = product;
        ticket.name = ticketDto.getName();
        ticket.price = ticketDto.getPrice();
        ticket.description = ticketDto.getDescription();
        return ticket;
    }
}
