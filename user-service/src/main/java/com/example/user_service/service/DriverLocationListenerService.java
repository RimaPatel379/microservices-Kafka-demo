package com.example.user_service.service;


import com.example.user_service.config.DriverLocationStore;
import com.example.user_service.model.DriverLocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverLocationListenerService {

    // In-memory store for the latest locations
    private final DriverLocationStore locationStore;
    /**
     * Listener for the "driver-location-updates" topic.
     * This uses the JSON → DriverLocation deserializer.
     *
     * groupId in annotation overrides config if specified, but here we keep
     * it consistent with the consumer factory group id.
     */
    @KafkaListener(
            topics = "driver-location-updates",
            groupId = "user-service-group",
            containerFactory = "driverLocationKafkaListenerContainerFactory"
    )
    public void consumeLocation(DriverLocation location) {
        log.info("Received location (driver-location-updates) for {}: lat={}, lon={}",
                location.getDriverId(),
                location.getLatitude(),
                location.getLongitude());

        // Update our in-memory latest location cache
        locationStore.updateLocation(location);
    }

    /**
     * Listener for the "cab-location" topic.
     * This also receives DriverLocation JSON messages.
     * Using a different group id means this listener has its own offset tracking.
     */
    @KafkaListener(
            topics = "cab-location",
            groupId = "user-group",
            containerFactory = "driverLocationKafkaListenerContainerFactory"
    )
    public void cabLocation(DriverLocation location) {
        log.info("Received location (cab-location) for {}: lat={}, lon={}",
                location.getDriverId(),
                location.getLatitude(),
                location.getLongitude());

        // Optionally store or process differently; here we reuse the same store.
        locationStore.updateLocation(location);
    }
}
