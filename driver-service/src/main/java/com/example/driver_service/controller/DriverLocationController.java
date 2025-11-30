package com.example.driver_service.controller;

import com.example.driver_service.model.DriverLocation;
import com.example.driver_service.service.DriverLocationProducerService;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverLocationController {

    // Service responsible for sending locations to Kafka
    private final DriverLocationProducerService producerService;

    /**
     * Endpoint to send a single driver location to Kafka as JSON.
     *
     * Example request:
     *   POST /api/drivers/driver-123/location
     *   {
     *     "latitude": 43.6532,
     *     "longitude": -79.3832
     *   }
     */
    @PostMapping("/{driverId}/location")
    public String sendLocation(@PathVariable String driverId,
                               @RequestBody LocationRequest request) {

        // Build the DriverLocation domain object from incoming DTO + current timestamp
        DriverLocation location = new DriverLocation(
                driverId,
                request.getLatitude(),
                request.getLongitude(),
                Instant.now()
        );

        // Publish to Kafka (driver-location-updates topic)
        producerService.sendLocation(location);
        return "Location sent to Kafka for driver " + driverId;
    }

    /**
     * Simple DTO representing the request body for location.
     * This is separate from the domain model DriverLocation.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class LocationRequest {
        private double latitude;
        private double longitude;
    }

    /**
     * Endpoint to simulate continuous driver location updates, like a real app.
     * It sends random coordinates to the 'cab-location' topic 100 times, 1 second apart.
     */
    @PutMapping("/updatelocation")
    public ResponseEntity<?> updateLocation() throws InterruptedException {
        int range = 100;
        while (range > 0){

            // Generate a fake driverId, e.g., driver-100, driver-99, ...
            String driverId = "driver-" + range;


            // Create a random location
            DriverLocation location = new DriverLocation(
                    driverId,
                    Math.random(),       // latitude
                    Math.random(),       // longitude
                    Instant.now()
            );

            // Send to cab-location topic
            producerService.updateLocation(location);

            // Sleep 1 second to simulate time passing between updates
            Thread.sleep(1000);
            range --;
        }// End of While
        return new ResponseEntity<>(Map.of("message","Location Updated"), HttpStatus.OK);
    }

}
