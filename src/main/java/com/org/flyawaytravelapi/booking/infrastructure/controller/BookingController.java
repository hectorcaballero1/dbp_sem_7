package com.org.flyawaytravelapi.booking.infrastructure.controller;

import com.org.flyawaytravelapi.booking.application.BookingService;
import com.org.flyawaytravelapi.booking.domain.Booking;
import com.org.flyawaytravelapi.booking.dto.request.FlightBookRequestDTO;
import com.org.flyawaytravelapi.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody FlightBookRequestDTO requestDTO) {
        try {
            // Obtener el usuario autenticado desde el contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();

            Booking booking = bookingService.createBooking(requestDTO.getFlightId(), authenticatedUser.getId());

            Map<String, String> response = new HashMap<>();
            response.put("id", booking.getId().toString());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.findById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", booking.getId());
        response.put("bookingDate", booking.getBookingDate());
        response.put("flightId", booking.getFlight().getId());
        response.put("flightNumber", booking.getFlight().getFlightNumber());
        response.put("customerId", booking.getUser().getId());
        response.put("customerFirstName", booking.getUser().getFirstName());
        response.put("customerLastName", booking.getUser().getLastName());

        return ResponseEntity.ok(response);
    }
}
