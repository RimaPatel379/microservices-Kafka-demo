# microservices-Kafka-demo
This is a basic project for understanding Kafka.

Cab-driver-service → sends driver location updates to Kafka.  
User-service → listens to those updates from Kafka and exposes a REST API so the user can see the latest location.  
