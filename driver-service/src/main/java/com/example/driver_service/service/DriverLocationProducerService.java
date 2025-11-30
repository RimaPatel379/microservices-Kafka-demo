package com.example.driver_service.service;

import com.example.driver_service.config.KafkaProducerConfig;
import com.example.driver_service.constant.AppConstant;
import com.example.driver_service.model.DriverLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverLocationProducerService {

    // KafkaTemplate for sending JSON DriverLocation messages
    private final KafkaTemplate<String, DriverLocation> kafkaTemplate;
    /**
     * Sends a DriverLocation object as JSON to the 'driver-location-updates' topic.
     * Key = driverId, so all events for the same driver go to the same partition.
     */
    public void sendLocation(DriverLocation location) {
        // Use driverId as the key so all messages for a driver go to same partition
        kafkaTemplate.send(AppConstant.DRIVER_LOCATION_TOPIC,
                location.getDriverId(),
                location
        );
    }

    /**
     * Simulate continuous driver location updates to cab-location topic.
     */
    public boolean updateLocation(DriverLocation location) {
        kafkaTemplate.send(AppConstant.CAB_LOCATION,
                location.getDriverId(),
                location
        );
        return true;
    }
}
