package com.aycan.airlineapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.aycan.airlineapi.entity.TicketPassenger;
import com.aycan.airlineapi.exception.AlreadyCheckedInException;
import com.aycan.airlineapi.exception.ResourceNotFoundException;
import com.aycan.airlineapi.repository.TicketPassengerRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkin")
public class CheckInController {

    private final TicketPassengerRepository ticketPassengerRepository;

    public CheckInController(TicketPassengerRepository ticketPassengerRepository) {
        this.ticketPassengerRepository = ticketPassengerRepository;
    }

    @PostMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public TicketPassenger checkIn(@PathVariable Long id) {
        TicketPassenger ticketPassenger = ticketPassengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TicketPassenger not found"));

        if (ticketPassenger.isCheckedIn()) {
            throw new AlreadyCheckedInException("Passenger already checked in");
        }

        ticketPassenger.setCheckedIn(true);
        ticketPassenger.setSeatNumber("1A");

        return ticketPassengerRepository.save(ticketPassenger);
    }
}