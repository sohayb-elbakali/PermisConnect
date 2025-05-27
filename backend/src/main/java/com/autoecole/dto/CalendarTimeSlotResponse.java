package com.autoecole.dto;

import com.autoecole.models.TimeSlot;
import lombok.Data;

@Data
public class CalendarTimeSlotResponse {
    private Long id;
    private String time; // Formatted time range
    private boolean available;
    private String instructorName;
    private String clientName; // If booked
    private String status;
    private Long reservationId; // If booked

    public static CalendarTimeSlotResponse fromTimeSlot(TimeSlot timeSlot) {
        CalendarTimeSlotResponse response = new CalendarTimeSlotResponse();
        response.setId(timeSlot.getId());
        response.setTime(String.format("%s - %s", 
            timeSlot.getStartTime().toLocalTime().toString(),
            timeSlot.getEndTime().toLocalTime().toString()));
        response.setAvailable(timeSlot.getStatus() == TimeSlot.TimeSlotStatus.AVAILABLE);
        response.setInstructorName(timeSlot.getMoniteur().getNom() + " " + timeSlot.getMoniteur().getPrenom());
        response.setStatus(timeSlot.getStatus().toString());

        if (timeSlot.getReservation() != null) {
            response.setClientName(timeSlot.getReservation().getClient().getUser().getNom() + " " + 
                                 timeSlot.getReservation().getClient().getUser().getPrenom());
            response.setReservationId(timeSlot.getReservation().getId());
        }

        return response;
    }
} 