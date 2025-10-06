package com.org.flyawaytravelapi.flight.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlightSearchResponseDTO {
    private List<FlightDTO> flights;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightDTO {
        private Long id;
        private String airlineName;
        private String flightNumber;
        private String estDepartureTime;
        private String estArrivalTime;
        private Integer availableSeats;
    }
}
