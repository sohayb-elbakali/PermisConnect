package com.autoecole.repositories;

import com.autoecole.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientId(Long clientId);
    List<Reservation> findByCoursId(Long coursId);
    List<Reservation> findByDateReservationBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByDateReservationAfter(LocalDateTime dateTime);
    List<Reservation> findByStatut(String statut);
}

