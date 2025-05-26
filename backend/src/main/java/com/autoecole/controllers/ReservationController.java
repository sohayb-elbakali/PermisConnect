package com.autoecole.controllers;

import com.autoecole.dto.CreateReservationRequest;
import com.autoecole.models.Reservation;
import com.autoecole.models.TimeSlot;
import com.autoecole.services.ReservationService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@Tag(name = "Reservation Management", description = "Operations related to reservations in the driving school system")
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary = "Create a new reservation",
               description = "Creates a new reservation for a client for a specific time slot")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservation created successfully",
                    content = @Content(schema = @Schema(implementation = Reservation.class))),
        @ApiResponse(responseCode = "400", description = "Invalid client or time slot ID provided"),
        @ApiResponse(responseCode = "404", description = "Client or time slot not found"),
        @ApiResponse(responseCode = "409", description = "Time slot is not available")
    })
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        Reservation reservation = reservationService.createReservation(request);
        return ResponseEntity.ok(reservation);
    }

    @Operation(summary = "Get all reservations for a client",
               description = "Retrieves all reservations made by a specific client")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Reservation>> getClientReservations(
            @Parameter(description = "ID of the client whose reservations to retrieve") @PathVariable Long clientId) {
        return ResponseEntity.ok(reservationService.getClientReservations(clientId));
    }

    @Operation(summary = "Get all reservations for a moniteur",
               description = "Retrieves all reservations made by a specific moniteur")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Moniteur not found")
    })
    @GetMapping("/moniteur/{moniteurId}")
    public ResponseEntity<List<Reservation>> getMoniteurReservations(
            @Parameter(description = "ID of the moniteur whose reservations to retrieve") @PathVariable Long moniteurId) {
        return ResponseEntity.ok(reservationService.getMoniteurReservations(moniteurId));
    }

    @Operation(summary = "Get all upcoming reservations",
               description = "Retrieves all reservations scheduled for future dates")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Upcoming reservations retrieved successfully")
    })
    @GetMapping("/upcoming")
    public ResponseEntity<List<Reservation>> getUpcomingReservations() {
        return ResponseEntity.ok(reservationService.getUpcomingReservations());
    }

    @Operation(summary = "Get reservations by date range",
               description = "Retrieves all reservations within a specified date range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<Reservation>> getReservationsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(reservationService.getReservationsByDateRange(startDate, endDate));
    }

    @Operation(summary = "Get available time slots for a moniteur",
               description = "Retrieves all available time slots for a specific moniteur on a given date")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Available time slots retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid moniteur ID or date"),
        @ApiResponse(responseCode = "404", description = "Moniteur not found")
    })
    @GetMapping("/available-slots/{moniteurId}")
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
            @Parameter(description = "ID of the moniteur") @PathVariable Long moniteurId,
            @Parameter(description = "Date to check") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(reservationService.getAvailableTimeSlots(moniteurId, date));
    }

    @Operation(summary = "Update reservation status",
               description = "Updates the status of an existing reservation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservation status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status provided"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @PutMapping("/{reservationId}/status")
    public ResponseEntity<Reservation> updateReservationStatus(
            @Parameter(description = "ID of the reservation to update") @PathVariable Long reservationId,
            @Parameter(description = "New status for the reservation (PENDING, BOOKED, CANCELLED)") 
            @RequestParam String status) {
        try {
            Reservation.ReservationStatus reservationStatus = Reservation.ReservationStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(reservationService.updateReservationStatus(reservationId, reservationStatus));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Must be one of: PENDING, BOOKED, CANCELLED");
        }
    }

    @Operation(summary = "Accept a reservation",
               description = "Accepts a pending reservation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservation accepted successfully"),
        @ApiResponse(responseCode = "400", description = "Reservation cannot be accepted"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @PutMapping("/{id}/accept")
    public ResponseEntity<Reservation> acceptReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.acceptReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @Operation(summary = "Cancel a reservation",
               description = "Cancels an existing reservation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservation canceled successfully"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }
}

