package com.autoecole.repositories;

import com.autoecole.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Check if there are any reservations for the given course and date
    boolean existsByTimeSlotIdAndDateReservation(Long timeSlotId, LocalDateTime dateReservation);
    
    // Find all reservations for a specific course
    List<Reservation> findByTimeSlotId(Long timeSlotId);
    
    // Find all reservations for a list of time slots
    List<Reservation> findByTimeSlotIdIn(List<Long> timeSlotIds);
    
    // Find all reservations for a specific client
    List<Reservation> findByClientId(Long clientId);

    List<Reservation> findByDateReservationBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Reservation> findByDateReservationAfter(LocalDateTime date);
    List<Reservation> findByMoniteurId(Long moniteurId);
}

