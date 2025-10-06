package com.org.flyawaytravelapi.booking.infrastructure.repository;

import com.org.flyawaytravelapi.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByFlightId(Long flightId);
    int countByFlightId(Long flightId);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND " +
            "((b.flight.estDepartureTime <= :arrivalTime AND b.flight.estArrivalTime >= :departureTime))")
    List<Booking> findOverlappingBookings(
            @Param("userId") Long userId,
            @Param("departureTime") LocalDateTime departureTime,
            @Param("arrivalTime") LocalDateTime arrivalTime
    );
}
