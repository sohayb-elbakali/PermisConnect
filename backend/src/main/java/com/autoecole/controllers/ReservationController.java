package com.autoecole.controllers;

import com.autoecole.models.Reservation;
import com.autoecole.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@Tag(name = "Reservation Management", description = "Operations related to reservations in the driving school system")
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary = "Create a new reservation",
               description = "Creates a new reservation for a client for a specific course")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservation created successfully",
                    content = @Content(schema = @Schema(implementation = Reservation.class))),
        @ApiResponse(responseCode = "400", description = "Invalid client or course ID provided"),
        @ApiResponse(responseCode = "404", description = "Client or course not found"),
        @ApiResponse(responseCode = "409", description = "Course is fully booked")
    })
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @Parameter(description = "ID of the client making the reservation") @RequestParam Long clientId,
            @Parameter(description = "ID of the course being reserved") @RequestParam Long coursId) {
        return ResponseEntity.ok(reservationService.createReservation(clientId, coursId));
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

    @Operation(summary = "Get all upcoming reservations",
               description = "Retrieves all reservations scheduled for future dates")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Upcoming reservations retrieved successfully")
    })
    @GetMapping("/upcoming")
    public ResponseEntity<List<Reservation>> getUpcomingReservations() {
        return ResponseEntity.ok(reservationService.getUpcomingReservations());
    }

    @Operation(summary = "Update reservation status",
               description = "Updates the status of an existing reservation (e.g., confirm or cancel)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reservation status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status provided"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @PutMapping("/{reservationId}/status")
    public ResponseEntity<Reservation> updateReservationStatus(
            @Parameter(description = "ID of the reservation to update") @PathVariable Long reservationId,
            @Parameter(description = "New status for the reservation (e.g., 'Confirmée', 'Annulée')") @RequestParam String status) {
        return ResponseEntity.ok(reservationService.updateReservationStatus(reservationId, status));
    }
}

