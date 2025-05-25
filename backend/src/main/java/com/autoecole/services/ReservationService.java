package com.autoecole.services;

import com.autoecole.models.Client;
import com.autoecole.models.Cours;
import com.autoecole.models.Reservation;
import com.autoecole.repositories.ClientRepository;
import com.autoecole.repositories.CoursRepository;
import com.autoecole.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CoursRepository coursRepository;
    private final ClientRepository clientRepository;

    public Reservation createReservation(Long clientId, Long coursId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Vérifier la disponibilité
        if (isCoursFull(coursId)) {
            throw new RuntimeException("Course is full");
        }

        Reservation reservation = new Reservation();
        reservation.setCours(cours);
        reservation.setClient(client);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut("En attente");

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getClientReservations(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    public List<Reservation> getUpcomingReservations() {
        return reservationRepository.findByDateReservationAfter(LocalDateTime.now());
    }

    public Reservation updateReservationStatus(Long reservationId, String status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatut(status);
        return reservationRepository.save(reservation);
    }

    private boolean isCoursFull(Long coursId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        int currentReservations = reservationRepository.findByCoursId(coursId).size();
        return currentReservations >= cours.getCapaciteMax();
    }
}

