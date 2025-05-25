package com.autoecole.controllers;

import com.autoecole.dto.request.MessageRequest;
import com.autoecole.dto.response.CommunicationResponse;
import com.autoecole.dto.response.MessageResponse;
import com.autoecole.models.Communication;
import com.autoecole.models.Message;
import com.autoecole.services.CommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/communications")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class CommunicationController {
    private final CommunicationService communicationService;

    @PostMapping
    public ResponseEntity<CommunicationResponse> createCommunication(
            @RequestParam Long clientId,
            @RequestParam String sujet) {
        Communication communication = communicationService.createCommunication(clientId, sujet);
        return ResponseEntity.ok(CommunicationResponse.fromEntity(communication));
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
        Message message = communicationService.sendMessage(
                request.getCommunicationId(),
                request.getContenu(),
                request.getExpediteur()
        );
        return ResponseEntity.ok(MessageResponse.fromEntity(message));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CommunicationResponse>> getClientCommunications(@PathVariable Long clientId) {
        List<Communication> communications = communicationService.getClientCommunications(clientId);
        List<CommunicationResponse> responses = communications.stream()
                .map(CommunicationResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{communicationId}/messages")
    public ResponseEntity<List<MessageResponse>> getCommunicationMessages(@PathVariable Long communicationId) {
        List<Message> messages = communicationService.getCommunicationMessages(communicationId);
        List<MessageResponse> responses = messages.stream()
                .map(MessageResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId) {
        communicationService.markMessageAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{communicationId}/messages/read-all")
    public ResponseEntity<Void> markAllMessagesAsRead(@PathVariable Long communicationId) {
        communicationService.markAllMessagesAsRead(communicationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{communicationId}/close")
    public ResponseEntity<Void> closeCommunication(@PathVariable Long communicationId) {
        communicationService.closeCommunication(communicationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/open")
    public ResponseEntity<List<CommunicationResponse>> getOpenCommunications() {
        List<Communication> communications = communicationService.getOpenCommunications();
        List<CommunicationResponse> responses = communications.stream()
                .map(CommunicationResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/messages/unread/{expediteur}")
    public ResponseEntity<List<MessageResponse>> getUnreadMessages(@PathVariable String expediteur) {
        List<Message> messages = communicationService.getUnreadMessages(expediteur);
        List<MessageResponse> responses = messages.stream()
                .map(MessageResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
} 