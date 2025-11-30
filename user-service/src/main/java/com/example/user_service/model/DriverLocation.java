package com.example.user_service.model;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverLocation {
    private String driverId;
    private double latitude;
    private double longitude;
    private Instant timestamp;
}
