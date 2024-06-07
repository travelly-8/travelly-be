package com.demo.travellybe.product.repository;

import com.demo.travellybe.product.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
