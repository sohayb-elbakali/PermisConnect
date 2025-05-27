package com.autoecole.controllers;

import com.autoecole.dto.CalendarTimeSlotResponse;
import com.autoecole.dto.CreateTimeSlotRequest;
import com.autoecole.dto.TimeSlotDTO;
import com.autoecole.models.TimeSlot;
import com.autoecole.services.TimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/time-slots")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@Tag(name = "Time Slot Management", description = "Operations related to time slots in the driving school system")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    @Operation(summary = "Create a new time slot",
               description = "Creates a new time slot for a moniteur")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Time slot created successfully",
                    content = @Content(schema = @Schema(implementation = TimeSlot.class))),
        @ApiResponse(responseCode = "400", description = "Invalid time slot data provided"),
        @ApiResponse(responseCode = "404", description = "Moniteur not found")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeSlot> createTimeSlot(@Valid @RequestBody CreateTimeSlotRequest request) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(request.getStartTime());
        timeSlot.setEndTime(request.getEndTime());
        timeSlot.setStatus(request.getStatus());
        return ResponseEntity.ok(timeSlotService.createTimeSlot(timeSlot, request.getMoniteurId()));
    }

    @Operation(summary = "Get all time slots for a moniteur",
               description = "Retrieves all time slots for a specific moniteur")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Time slots retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Moniteur not found")
    })
    @GetMapping("/moniteur/{moniteurId}")
    public ResponseEntity<List<TimeSlotDTO>> getMoniteurTimeSlots(@PathVariable Long moniteurId) {
        return ResponseEntity.ok(
            timeSlotService.getMoniteurTimeSlots(moniteurId)
                .stream()
                .map(slot -> {
                    TimeSlotDTO dto = new TimeSlotDTO();
                    dto.setId(slot.getId());
                    dto.setStartTime(slot.getStartTime() != null ? slot.getStartTime().toString() : null);
                    dto.setEndTime(slot.getEndTime() != null ? slot.getEndTime().toString() : null);
                    dto.setStatus(slot.getStatus() != null ? slot.getStatus().toString() : null);
                    dto.setInstructor(slot.getMoniteur() != null ? slot.getMoniteur().getPrenom() + " " + slot.getMoniteur().getNom() : null);
                    return dto;
                })
                .collect(Collectors.toList())
        );
    }

    @Operation(summary = "Get time slots by date range",
               description = "Retrieves all time slots within a specified date range for a moniteur")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Time slots retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date format"),
        @ApiResponse(responseCode = "404", description = "Moniteur not found")
    })
    @GetMapping("/moniteur/{moniteurId}/date-range")
    public ResponseEntity<List<TimeSlot>> getTimeSlotsByDateRange(
            @Parameter(description = "ID of the moniteur") @PathVariable Long moniteurId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(timeSlotService.getTimeSlotsByDateRange(moniteurId, startDate, endDate));
    }

    @Operation(summary = "Update time slot status",
               description = "Updates the status of an existing time slot")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Time slot status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status provided"),
        @ApiResponse(responseCode = "404", description = "Time slot not found")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<TimeSlot> updateTimeSlotStatus(
            @Parameter(description = "ID of the time slot to update") @PathVariable Long id,
            @Parameter(description = "New status for the time slot (AVAILABLE, BOOKED, CANCELLED)") 
            @RequestParam String status) {
        try {
            TimeSlot.TimeSlotStatus timeSlotStatus = TimeSlot.TimeSlotStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(timeSlotService.updateTimeSlotStatus(id, timeSlotStatus));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Must be one of: AVAILABLE, BOOKED, CANCELLED");
        }
    }

    @Operation(summary = "Delete a time slot",
               description = "Deletes an existing time slot")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Time slot deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Time slot not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(
            @Parameter(description = "ID of the time slot to delete") @PathVariable Long id) {
        timeSlotService.deleteTimeSlot(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get calendar data for a specific date",
               description = "Retrieves all time slots and reservations for a specific moniteur on a given date")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Calendar data retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date format"),
        @ApiResponse(responseCode = "404", description = "Moniteur not found")
    })
    @GetMapping("/calendar/{moniteurId}")
    public ResponseEntity<List<CalendarTimeSlotResponse>> getCalendarData(
            @Parameter(description = "ID of the moniteur") @PathVariable Long moniteurId,
            @Parameter(description = "Date to check (format: yyyy-MM-dd)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date) {
        return ResponseEntity.ok(timeSlotService.getCalendarData(moniteurId, date));
    }
} 