package com.example.driver_service.model;

import lombok.*;

import java.time.Instant;

@Data // sometimes it don't generate all things
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverLocation {
    private String driverId;
    private double latitude;
    private double longitude;
    private Instant timestamp;
}
