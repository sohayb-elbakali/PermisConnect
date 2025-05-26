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
    public TimeSlot createTimeSlot(TimeSlot timeSlot) {
        // Verify that the moniteur exists
        moniteurRepository.findById(timeSlot.getMoniteur().getId())
                .orElseThrow(() -> new EntityNotFoundException("Moniteur not found"));

        // Set default status if not provided
        if (timeSlot.getStatus() == null) {
            timeSlot.setStatus(TimeSlot.TimeSlotStatus.AVAILABLE);
        }

        return timeSlotRepository.save(timeSlot);
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