package com.demo.travellybe.product.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class TicketPrice {
    private int adult;
    private int child;
    private int teen;
}