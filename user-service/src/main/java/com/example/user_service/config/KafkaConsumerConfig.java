package com.example.user_service.config;

import com.example.user_service.model.DriverLocation;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    /**
     * ConsumerFactory for consuming DriverLocation JSON messages.
     * - Key: String
     * - Value: DriverLocation (JacksonJsonDeserializer)
     */
    @Bean
    public ConsumerFactory<String, DriverLocation> driverLocationConsumerFactory() {
        JacksonJsonDeserializer<DriverLocation> deserializer =
                new JacksonJsonDeserializer<>(DriverLocation.class,false);
        deserializer.addTrustedPackages("*");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "user-service-group");

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    /**
     * Kafka listener container factory that uses the above ConsumerFactory.
     * This factory will be referenced in @KafkaListener.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DriverLocation> driverLocationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DriverLocation> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(driverLocationConsumerFactory());
        return factory;
    }

}
