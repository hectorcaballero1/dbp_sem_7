package com.org.flyawaytravelapi.booking.application;

import com.org.flyawaytravelapi.booking.domain.Booking;
import com.org.flyawaytravelapi.booking.domain.event.BookingCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class EmailService {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @EventListener
    public void handleBookingCreated(BookingCreatedEvent event) {
        Booking booking = event.getBooking();

        String fileName = String.format("flight_booking_email_%d.txt", booking.getId());

        String emailContent = String.format(
            "Hello %s %s,\n\n" +
            "Your booking was successful!\n\n" +
            "The booking is for flight %s with departure date of %s and arrival date of %s.\n\n" +
            "The booking was registered at %s.\n\n" +
            "Bon Voyage!\n" +
            "Fly Away Travel",
            booking.getUser().getFirstName(),
            booking.getUser().getLastName(),
            booking.getFlight().getFlightNumber(),
            booking.getFlight().getEstDepartureTime().format(ISO_FORMATTER),
            booking.getFlight().getEstArrivalTime().format(ISO_FORMATTER),
            booking.getBookingDate().format(ISO_FORMATTER)
        );

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(emailContent);
            log.info("Booking confirmation email saved to: {}", fileName);
        } catch (IOException e) {
            log.error("Failed to write booking email file", e);
        }
    }
}
