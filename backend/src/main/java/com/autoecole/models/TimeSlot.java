package com.autoecole.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "time_slots")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "moniteur_id")
    private Moniteur moniteur;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private TimeSlotStatus status = TimeSlotStatus.AVAILABLE;

    @OneToOne(mappedBy = "timeSlot")
    private Reservation reservation;

    public enum TimeSlotStatus {
        AVAILABLE,
        BOOKED,
        CANCELLED
    }
} 