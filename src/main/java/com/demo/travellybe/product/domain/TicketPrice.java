package com.demo.travellybe.product.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
public class TicketPrice {
    private int adult;
    private int child;
    private int teen;

    public TicketPrice(int adult, int child, int teen) {
        this.adult = adult;
        this.child = child;
        this.teen = teen;
    }
}