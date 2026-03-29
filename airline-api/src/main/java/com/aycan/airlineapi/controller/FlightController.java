package com.aycan.airlineapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.aycan.airlineapi.entity.Flight;
import com.aycan.airlineapi.entity.Ticket;
import com.aycan.airlineapi.entity.TicketPassenger;
import com.aycan.airlineapi.exception.ResourceNotFoundException;
import com.aycan.airlineapi.repository.FlightRepository;
import com.aycan.airlineapi.repository.TicketPassengerRepository;
import com.aycan.airlineapi.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final TicketPassengerRepository ticketPassengerRepository;

    public FlightController(FlightRepository flightRepository,
                            TicketRepository ticketRepository,
                            TicketPassengerRepository ticketPassengerRepository) {
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
        this.ticketPassengerRepository = ticketPassengerRepository;
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public Flight addFlight(@RequestBody Flight flight) {
        return flightRepository.save(flight);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Upload flights from CSV file")
    public String uploadFlights(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (firstLine) {
                    firstLine = false;
                    if (line.toLowerCase().contains("flightnumber")) {
                        continue;
                    }
                }

                String[] data = line.split(",");

                if (data.length != 7) {
                    continue;
                }

                Flight flight = new Flight();
                flight.setFlightNumber(data[0].trim());
                flight.setDepartureDateTime(data[1].trim());
                flight.setArrivalDateTime(data[2].trim());
                flight.setAirportFrom(data[3].trim());
                flight.setAirportTo(data[4].trim());
                flight.setDurationMinutes(Integer.parseInt(data[5].trim()));
                flight.setCapacity(Integer.parseInt(data[6].trim()));

                flightRepository.save(flight);
                count++;
            }

            return count + " flights uploaded successfully";
        } catch (Exception e) {
            return "Error while processing file: " + e.getMessage();
        }
    }

    @GetMapping
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @GetMapping("/search")
public List<Flight> searchFlights(
        @RequestParam String from,
        @RequestParam String to,
        @RequestParam String departureDate,
        @RequestParam(defaultValue = "1") int numberOfPeople,
        @RequestParam(required = false) String tripType,
        @RequestParam(required = false) String returnDate) {

    return flightRepository
            .findByAirportFromAndAirportToAndDepartureDateTimeContainingAndAvailableSeatsGreaterThanEqual(
                    from,
                    to,
                    departureDate,
                    numberOfPeople
            );
}

    @GetMapping("/page")
    public Page<Flight> getFlightsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return flightRepository.findAll(pageable);
    }

    @GetMapping("/{flightId}/passengers")
    @SecurityRequirement(name = "bearerAuth")
    public List<TicketPassenger> getPassengerList(@PathVariable Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        List<Ticket> tickets = ticketRepository.findByFlight(flight);
        return ticketPassengerRepository.findByTicketIn(tickets);
    }
}