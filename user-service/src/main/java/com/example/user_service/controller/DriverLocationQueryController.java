package com.example.user_service.controller;

import com.example.user_service.config.DriverLocationStore;
import com.example.user_service.model.DriverLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class DriverLocationQueryController {

    // In-memory store that holds latest locations for drivers
    private final DriverLocationStore locationStore;

    /**
     * Example:
     *   GET /api/users/drivers/driver-123/location
     */
    @GetMapping("/drivers/{driverId}/location")
    public ResponseEntity<?> getDriverLocation(@PathVariable String driverId) {
        DriverLocation location = locationStore.getLatestLocation(driverId);
        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(location);
    }
}
