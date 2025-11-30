package com.example.user_service.config;

import com.example.user_service.model.DriverLocation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory store to keep the latest known location per driver.
 * This simulates a cache or fast lookup store.
 */
@Component
public class DriverLocationStore {
    // key: driverId, value: latest known location
    private final Map<String, DriverLocation> latestLocations = new ConcurrentHashMap<>();

    /**
     * Update or insert the latest location for a driver.
     */
    public void updateLocation(DriverLocation location) {
        latestLocations.put(location.getDriverId(), location);
    }

    /**
     * Retrieve the last known location for the given driverId.
     * Returns null if not found.
     */
    public DriverLocation getLatestLocation(String driverId) {
        return latestLocations.get(driverId);
    }
}
