package com.aycan.airlineapi.repository;

import com.aycan.airlineapi.entity.Flight;
import com.aycan.airlineapi.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByFlight(Flight flight);
}