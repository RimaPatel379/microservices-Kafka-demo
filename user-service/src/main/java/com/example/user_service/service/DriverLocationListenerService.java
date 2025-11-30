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

    // Json DriverLocation
    @KafkaListener(
            topics = "driver-location-updates",
            groupId = "user-service-group",
            containerFactory = "driverLocationKafkaListenerContainerFactory"
    )
    public void consumeLocation(DriverLocation location) {
        log.info("Received location for {}: lat={}, lon={}",
                location.getDriverId(),
                location.getLatitude(),
                location.getLongitude());

        locationStore.updateLocation(location);
    }


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

        // you can also store these if you want
        locationStore.updateLocation(location);
    }
}
