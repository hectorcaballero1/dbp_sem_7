package com.org.flyawaytravelapi.flight.infrastructure.controller;

import com.org.flyawaytravelapi.flight.application.FlightService;
import com.org.flyawaytravelapi.flight.domain.Flight;
import com.org.flyawaytravelapi.flight.dto.request.NewFlightRequestDTO;
import com.org.flyawaytravelapi.flight.dto.response.FlightSearchResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NewFlightRequestDTO newFlight) {
        try {
            Flight flight = flightService.createFlight(newFlight);
            Map<String, String> response = new HashMap<>();
            response.put("id", flight.getId().toString());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<FlightSearchResponseDTO> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) String estDepartureTimeFrom,
            @RequestParam(required = false) String estDepartureTimeTo) {

        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;

        if (estDepartureTimeFrom != null && !estDepartureTimeFrom.isEmpty()) {
            fromDate = LocalDateTime.parse(estDepartureTimeFrom);
        }
        if (estDepartureTimeTo != null && !estDepartureTimeTo.isEmpty()) {
            toDate = LocalDateTime.parse(estDepartureTimeTo);
        }

        List<Flight> flights = flightService.searchFlights(flightNumber, airlineName, fromDate, toDate);

        List<FlightSearchResponseDTO.FlightDTO> flightDTOs = flights.stream()
                .map(flight -> {
                    FlightSearchResponseDTO.FlightDTO dto = new FlightSearchResponseDTO.FlightDTO();
                    dto.setId(flight.getId());
                    dto.setAirlineName(flight.getAirlineName());
                    dto.setFlightNumber(flight.getFlightNumber());
                    dto.setEstDepartureTime(flight.getEstDepartureTime().toString());
                    dto.setEstArrivalTime(flight.getEstArrivalTime().toString());
                    dto.setAvailableSeats(flight.getAvailableSeats());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new FlightSearchResponseDTO(flightDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        Flight flight = flightService.findById(id);
        if (flight == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(flight);
    }
}
