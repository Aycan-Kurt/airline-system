package com.aycan.airlineapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.aycan.airlineapi.dto.TicketRequest;
import com.aycan.airlineapi.entity.Flight;
import com.aycan.airlineapi.entity.Passenger;
import com.aycan.airlineapi.entity.Ticket;
import com.aycan.airlineapi.entity.TicketPassenger;
import com.aycan.airlineapi.exception.ResourceNotFoundException;
import com.aycan.airlineapi.exception.SoldOutException;
import com.aycan.airlineapi.repository.FlightRepository;
import com.aycan.airlineapi.repository.PassengerRepository;
import com.aycan.airlineapi.repository.TicketPassengerRepository;
import com.aycan.airlineapi.repository.TicketRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final TicketPassengerRepository ticketPassengerRepository;

    public TicketController(TicketRepository ticketRepository,
                            FlightRepository flightRepository,
                            PassengerRepository passengerRepository,
                            TicketPassengerRepository ticketPassengerRepository) {
        this.ticketRepository = ticketRepository;
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
        this.ticketPassengerRepository = ticketPassengerRepository;
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public Ticket buyTicket(@RequestBody TicketRequest request) {
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        if (flight.getAvailableSeats() <= 0) {
            throw new SoldOutException("Sold out");
        }

        Ticket ticket = new Ticket();
        ticket.setFlight(flight);
        ticket.setTicketNumber(request.getTicketNumber());
        ticket.setStatus(request.getStatus());
        Ticket savedTicket = ticketRepository.save(ticket);

        Passenger passenger = new Passenger();
        passenger.setFullName(request.getPassengerName());
        Passenger savedPassenger = passengerRepository.save(passenger);

        TicketPassenger ticketPassenger = new TicketPassenger();
        ticketPassenger.setTicket(savedTicket);
        ticketPassenger.setPassenger(savedPassenger);
        ticketPassenger.setCheckedIn(false);
        ticketPassenger.setSeatNumber(null);
        ticketPassengerRepository.save(ticketPassenger);

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        return savedTicket;
    }
}