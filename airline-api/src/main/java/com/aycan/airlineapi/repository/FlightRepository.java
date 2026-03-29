package com.aycan.airlineapi.repository;

import com.aycan.airlineapi.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    
    List<Flight> findByAirportFromAndAirportToAndAvailableSeatsGreaterThan(
            String airportFrom,
            String airportTo,
            int availableSeats
    );

    
    List<Flight> findByAirportFromAndAirportToAndDepartureDateTimeContainingAndAvailableSeatsGreaterThanEqual(
            String airportFrom,
            String airportTo,
            String departureDateTime,
            int availableSeats
    );
}