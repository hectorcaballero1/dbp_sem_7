package com.org.flyawaytravelapi.flight.application;

import com.org.flyawaytravelapi.flight.domain.Flight;
import com.org.flyawaytravelapi.flight.dto.request.NewFlightRequestDTO;
import com.org.flyawaytravelapi.flight.infrastructure.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;

    private static final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("^[A-Z0-9]{1,6}$");

    public Flight createFlight(NewFlightRequestDTO flightDTO) {
        // Validar campos obligatorios
        if (flightDTO.getAirlineName() == null || flightDTO.getAirlineName().trim().isEmpty()) {
            throw new IllegalArgumentException("Airline name is required");
        }
        if (flightDTO.getFlightNumber() == null || flightDTO.getFlightNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Flight number is required");
        }
        if (flightDTO.getEstDepartureTime() == null) {
            throw new IllegalArgumentException("Estimated departure time is required");
        }
        if (flightDTO.getEstArrivalTime() == null) {
            throw new IllegalArgumentException("Estimated arrival time is required");
        }
        if (flightDTO.getAvailableSeats() == null) {
            throw new IllegalArgumentException("Available seats is required");
        }

        // Validar formato de flight number (A-Z 0-9, hasta 6 caracteres)
        if (!FLIGHT_NUMBER_PATTERN.matcher(flightDTO.getFlightNumber()).matches()) {
            throw new IllegalArgumentException("Flight number must be A-Z 0-9, up to 6 characters");
        }

        // Validar que el flight number no esté repetido
        if (flightRepository.existsByFlightNumber(flightDTO.getFlightNumber())) {
            throw new IllegalArgumentException("Flight number already exists");
        }

        // Validar que estDepartureTime < estArrivalTime
        if (!flightDTO.getEstDepartureTime().isBefore(flightDTO.getEstArrivalTime())) {
            throw new IllegalArgumentException("Departure time must be before arrival time");
        }

        // Validar que availableSeats > 0
        if (flightDTO.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("Available seats must be greater than 0");
        }

        Flight flight = new Flight();
        flight.setAirlineName(flightDTO.getAirlineName());
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setEstDepartureTime(flightDTO.getEstDepartureTime());
        flight.setEstArrivalTime(flightDTO.getEstArrivalTime());
        flight.setAvailableSeats(flightDTO.getAvailableSeats());

        return flightRepository.save(flight);
    }

    public List<Flight> searchFlights(String flightNumber, String airlineName,
                                      LocalDateTime fromDate, LocalDateTime toDate) {
        return flightRepository.searchFlights(flightNumber, airlineName, fromDate, toDate);
    }

    public Flight findById(Long id) {
        return flightRepository.findById(id).orElse(null);
    }

    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    public void deleteAll() {
        flightRepository.deleteAll();
    }
}
