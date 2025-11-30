package com.example.driver_service.config;

import com.example.driver_service.constant.AppConstant;
import com.example.driver_service.model.DriverLocation;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    /**
     * ProducerFactory for sending DriverLocation objects as JSON.
     * - Key: String (driverId)
     * - Value: DriverLocation (serialized with JacksonJsonSerializer)
     */
    @Bean
    public ProducerFactory<String, DriverLocation> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        // Kafka broker address
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Key serializer: convert String key to bytes
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Value serializer: convert DriverLocation object to JSON bytes
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * KafkaTemplate for sending DriverLocation messages using the above ProducerFactory.
     */
    @Bean
    public KafkaTemplate<String, DriverLocation> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }




    /**
     * Topic for driver location updates.
     * This bean will auto-create the topic if Kafka Admin is enabled.
     */
    @Bean
    public NewTopic driverLocationTopic() {
        return TopicBuilder.name(AppConstant.DRIVER_LOCATION_TOPIC)
                .partitions(3) // For scalability and parallelism
                .replicas(1)   // Single replica for local dev
                .build();
    }

    /**
     * Topic for "cab-location" events.
     * In this demo, we also send DriverLocation JSON to this topic.
     */
    @Bean
    public NewTopic cabLocationTopic() {
        return TopicBuilder
                .name(AppConstant.CAB_LOCATION)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
