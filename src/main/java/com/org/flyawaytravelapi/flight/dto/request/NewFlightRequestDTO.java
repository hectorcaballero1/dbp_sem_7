package com.org.flyawaytravelapi.flight.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewFlightRequestDTO {
    private String airlineName;
    private String flightNumber;
    private LocalDateTime estDepartureTime;
    private LocalDateTime estArrivalTime;
    private Integer availableSeats;
}
