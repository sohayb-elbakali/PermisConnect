package com.autoecole.repositories;

import com.autoecole.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByMoniteurIdAndStartTimeBetween(Long moniteurId, LocalDateTime start, LocalDateTime end);
    List<TimeSlot> findByMoniteur_AutoEcoleIdAndStartTimeBetween(Long autoEcoleId, LocalDateTime start, LocalDateTime end);
    List<TimeSlot> findByStatus(TimeSlot.TimeSlotStatus status);
    List<TimeSlot> findByMoniteurId(Long moniteurId);
    
    // Find time slots for a moniteur on a specific date
    default List<TimeSlot> findByMoniteurIdAndDate(Long moniteurId, LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        return findByMoniteurIdAndStartTimeBetween(moniteurId, startOfDay, endOfDay);
    }
} 