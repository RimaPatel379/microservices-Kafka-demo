
#  Real-Time Driver Tracking System (Spring Boot + Kafka)

A complete **event-driven microservices project** for tracking driver locations in real-time using **Spring Boot**, **Apache Kafka**, and REST APIs.

This system simulates how ride-sharing apps (Uber, Lyft), logistics platforms, or BFSI location-based services handle **live tracking**.

##  Microservices Overview

### **1️⃣ driver-service**
- Produces driver location updates  
- Publishes JSON events to Kafka  
- Provides REST APIs to submit or simulate location updates  

### **2️⃣ user-service**
- Consumes driver location updates  
- Stores the **latest location** per driver  
- Exposes REST API to query driver locations  

### **3️⃣ Kafka**
- Acts as message broker between both services  
- Topics used:  
  - `driver-location-updates`  
  - `cab-location`  

## Architecture Diagram

```
              ┌────────────────────┐
              │   driver-service   │
              │--------------------│
              │ REST API → Kafka   │
              │ Sends JSON events  │
              └───────────┬────────┘
                          │
                          ▼
                   ┌────────────┐
                   │   Kafka    │
                   │------------│
                   │ Topics:    │
                   │ 1) driver-location-updates
                   │ 2) cab-location
                   └───────┬────┘
                           │
                           ▼
               ┌─────────────────────┐
               │    user-service     │
               │---------------------│
               │ Kafka Listener      │
               │ Stores Locations    │
               │ REST Query API      │
               └─────────────────────┘
```

##  Technologies Used

- **Java 17**
- **Spring Boot 3**
- **Spring Kafka**
- **Apache Kafka**
- **Lombok**
- **REST APIs**
- **JSON Serialization / Deserialization**

##  DriverLocation JSON Structure

```json
{
  "driverId": "driver-1",
  "latitude": 43.6532,
  "longitude": -79.3832,
  "timestamp": "2025-01-01T12:34:56Z"
}
```

##  API Endpoints

###  Driver-Service (PORT: 8081)

#### **1. Send a single location update**
```
POST /api/drivers/{driverId}/location
```

Body:
```json
{
  "latitude": 43.6532,
  "longitude": -79.3832
}
```

#### **2. Simulate 100 continuous location updates**
```
PUT /api/drivers/updatelocation
```

###  User-Service (PORT: 8082)

#### **1. Get latest driver location**
```
GET /api/users/drivers/{driverId}/location
```

Response:
```json
{
  "driverId": "driver-1",
  "latitude": 43.6532,
  "longitude": -79.3832,
  "timestamp": "2025-01-01T12:34:56Z"
}
```

## ▶️ How to Run the Project

### **1️⃣ Start Kafka using Docker**
```bash
docker-compose up -d
```

### **2️⃣ Start Services**

#### Driver Service
```bash
cd driver-service
mvn spring-boot:run
```

#### User Service
```bash
cd user-service
mvn spring-boot:run
```




