package com.autoecole.dto;

import lombok.Data;

@Data
public class TimeSlotDTO {
    private Long id;
    private String startTime;
    private String endTime;
    private String status;
    private String instructor;
} 