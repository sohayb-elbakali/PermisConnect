package com.autoecole.services;

import com.autoecole.dto.CreateReservationRequest;
import com.autoecole.models.*;
import com.autoecole.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final MoniteurRepository moniteurRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Transactional
    public Reservation createReservation(CreateReservationRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new EntityNotFoundException("Time slot not found"));
        
        Moniteur moniteur = timeSlot.getMoniteur();
        
        // Check if the time slot is already booked
        if (reservationRepository.existsByTimeSlotIdAndDateReservation(timeSlot.getId(), timeSlot.getStartTime())) {
            throw new IllegalStateException("Time slot is not available");
        }
        
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setMoniteur(moniteur);
        reservation.setTimeSlot(timeSlot);
        reservation.setDateReservation(timeSlot.getStartTime());
        reservation.setStatut(Reservation.ReservationStatus.PENDING);
        
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getClientReservations(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    public List<Reservation> getMoniteurReservations(Long moniteurId) {
        return reservationRepository.findByMoniteurId(moniteurId);
    }

    public List<Reservation> getUpcomingReservations() {
        return reservationRepository.findByDateReservationAfter(LocalDateTime.now());
    }

    public List<Reservation> getReservationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return reservationRepository.findByDateReservationBetween(startDate, endDate);
    }

    public List<TimeSlot> getAvailableTimeSlots(Long moniteurId, LocalDateTime date) {
        Moniteur moniteur = moniteurRepository.findById(moniteurId)
                .orElseThrow(() -> new EntityNotFoundException("Moniteur not found"));
        
        // Get all time slots for the moniteur on the given date
        List<TimeSlot> allTimeSlots = timeSlotRepository.findByMoniteurIdAndDate(moniteurId, date);
        
        // Get all reservations for these time slots
        List<Reservation> reservations = reservationRepository.findByTimeSlotIdIn(
            allTimeSlots.stream().map(TimeSlot::getId).toList()
        );
        
        // Filter out time slots that are already booked
        return allTimeSlots.stream()
            .filter(timeSlot -> reservations.stream()
                .noneMatch(reservation -> reservation.getTimeSlot().getId().equals(timeSlot.getId())))
            .toList();
    }

    @Transactional
    public Reservation updateReservationStatus(Long id, Reservation.ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        
        reservation.setStatut(status);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation acceptReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        
        if (reservation.getStatut() != Reservation.ReservationStatus.PENDING) {
            throw new IllegalStateException("Only pending reservations can be accepted");
        }
        
        reservation.setStatut(Reservation.ReservationStatus.BOOKED);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        
        if (reservation.getStatut() == Reservation.ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservation is already cancelled");
        }
        
        reservation.setStatut(Reservation.ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }
}

