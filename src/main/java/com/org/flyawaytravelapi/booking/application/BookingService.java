package com.org.flyawaytravelapi.booking.application;

import com.org.flyawaytravelapi.booking.domain.Booking;
import com.org.flyawaytravelapi.booking.domain.event.BookingCreatedEvent;
import com.org.flyawaytravelapi.booking.infrastructure.repository.BookingRepository;
import com.org.flyawaytravelapi.flight.application.FlightService;
import com.org.flyawaytravelapi.flight.domain.Flight;
import com.org.flyawaytravelapi.user.application.UserService;
import com.org.flyawaytravelapi.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightService flightService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Booking createBooking(Long flightId, Long userId) {
        Flight flight = flightService.findById(flightId);
        if (flight == null) {
            throw new IllegalArgumentException("Flight not found");
        }

        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Validar que hay asientos disponibles
        if (flight.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("No available seats");
        }

        // Nice-to-have: Validar que el vuelo no esté en el pasado o en tránsito
        LocalDateTime now = LocalDateTime.now();
        if (flight.getEstDepartureTime().isBefore(now)) {
            throw new IllegalArgumentException("Cannot book a flight in the past or in transit");
        }

        // Nice-to-have: Validar que el cliente no tenga vuelos solapados
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                userId,
                flight.getEstDepartureTime(),
                flight.getEstArrivalTime()
        );
        if (!overlappingBookings.isEmpty()) {
            throw new IllegalArgumentException("Customer already has a booking that overlaps with this flight");
        }

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setUser(user);
        booking.setBookingDate(LocalDateTime.now());

        // Decrementar asientos disponibles
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightService.save(flight);

        Booking savedBooking = bookingRepository.save(booking);

        // Publicar evento de booking creado
        eventPublisher.publishEvent(new BookingCreatedEvent(this, savedBooking));

        return savedBooking;
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public void deleteAll() {
        bookingRepository.deleteAll();
    }
}
