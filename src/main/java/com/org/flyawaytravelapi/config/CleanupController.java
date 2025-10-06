package com.org.flyawaytravelapi.config;

import com.org.flyawaytravelapi.booking.application.BookingService;
import com.org.flyawaytravelapi.flight.application.FlightService;
import com.org.flyawaytravelapi.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cleanup")
@RequiredArgsConstructor
public class CleanupController {

    private final UserService userService;
    private final FlightService flightService;
    private final BookingService bookingService;

    @DeleteMapping
    public ResponseEntity<String> cleanup() {
        try {
            bookingService.deleteAll();
            userService.deleteAll();
            flightService.deleteAll();
            return ResponseEntity.ok("Cleanup completed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Cleanup failed: " + e.getMessage());
        }
    }
}
