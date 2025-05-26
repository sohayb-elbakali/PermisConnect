package com.autoecole.services;

import com.autoecole.models.TimeSlot;
import com.autoecole.repositories.MoniteurRepository;
import com.autoecole.repositories.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final MoniteurRepository moniteurRepository;

    @Transactional
    public TimeSlot createTimeSlot(TimeSlot timeSlot, Long moniteurId) {
        // Verify that the moniteur exists
        var moniteur = moniteurRepository.findById(moniteurId)
                .orElseThrow(() -> new EntityNotFoundException("Moniteur not found"));
        
        // Validate time slot
        validateTimeSlot(timeSlot);
        
        // Check for overlapping time slots
        validateNoOverlappingTimeSlots(timeSlot, moniteurId);
        
        timeSlot.setMoniteur(moniteur);
        timeSlot.setId(null); // Ensure new ID is generated

        // Set default status if not provided
        if (timeSlot.getStatus() == null) {
            timeSlot.setStatus(TimeSlot.TimeSlotStatus.AVAILABLE);
        }

        return timeSlotRepository.save(timeSlot);
    }

    private void validateTimeSlot(TimeSlot timeSlot) {
        if (timeSlot.getStartTime() == null || timeSlot.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required");
        }

        if (timeSlot.getStartTime().isAfter(timeSlot.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (timeSlot.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create time slots in the past");
        }
    }

    private void validateNoOverlappingTimeSlots(TimeSlot newTimeSlot, Long moniteurId) {
        List<TimeSlot> existingTimeSlots = timeSlotRepository.findByMoniteurIdAndStartTimeBetween(
            moniteurId,
            newTimeSlot.getStartTime().minusMinutes(1),
            newTimeSlot.getEndTime().plusMinutes(1)
        );

        for (TimeSlot existingSlot : existingTimeSlots) {
            if (isOverlapping(newTimeSlot, existingSlot)) {
                throw new IllegalStateException("Time slot overlaps with existing time slot");
            }
        }
    }

    private boolean isOverlapping(TimeSlot newSlot, TimeSlot existingSlot) {
        return !newSlot.getEndTime().isBefore(existingSlot.getStartTime()) &&
               !newSlot.getStartTime().isAfter(existingSlot.getEndTime());
    }

    public List<TimeSlot> getMoniteurTimeSlots(Long moniteurId) {
        // Verify that the moniteur exists
        moniteurRepository.findById(moniteurId)
                .orElseThrow(() -> new EntityNotFoundException("Moniteur not found"));

        return timeSlotRepository.findByMoniteurId(moniteurId);
    }

    public List<TimeSlot> getTimeSlotsByDateRange(Long moniteurId, LocalDateTime startDate, LocalDateTime endDate) {
        // Verify that the moniteur exists
        moniteurRepository.findById(moniteurId)
                .orElseThrow(() -> new EntityNotFoundException("Moniteur not found"));

        return timeSlotRepository.findByMoniteurIdAndStartTimeBetween(moniteurId, startDate, endDate);
    }

    @Transactional
    public TimeSlot updateTimeSlotStatus(Long id, TimeSlot.TimeSlotStatus status) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Time slot not found"));

        timeSlot.setStatus(status);
        return timeSlotRepository.save(timeSlot);
    }

    @Transactional
    public void deleteTimeSlot(Long id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Time slot not found"));

        timeSlotRepository.delete(timeSlot);
    }
} 