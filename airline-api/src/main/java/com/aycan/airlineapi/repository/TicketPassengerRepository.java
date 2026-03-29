package com.aycan.airlineapi.repository;

import com.aycan.airlineapi.entity.Ticket;
import com.aycan.airlineapi.entity.TicketPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketPassengerRepository extends JpaRepository<TicketPassenger, Long> {
    List<TicketPassenger> findByTicketIn(List<Ticket> tickets);
}