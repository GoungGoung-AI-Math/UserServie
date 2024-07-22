package com.example.demo.my.kafka.infra.kafka.producer.exception;

public class KafkaProducerException extends RuntimeException {

    public KafkaProducerException(String message) {
        super(message);
    }
}
