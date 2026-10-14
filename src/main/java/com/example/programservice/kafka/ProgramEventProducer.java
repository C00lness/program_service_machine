package com.example.programservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProgramEventProducer {

    private static final String TOPIC = "program-events";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProgramEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProgramCreatedEvent(Long programId, String programName, String createdBy) {
        String message = String.format(
                "{\"event\":\"PROGRAM_CREATED\",\"programId\":%d,\"programName\":\"%s\",\"createdBy\":\"%s\"}",
                programId, programName, createdBy
        );
        kafkaTemplate.send(TOPIC, message);
        System.out.println("📤 Событие отправлено в Kafka: " + message);
    }
}

