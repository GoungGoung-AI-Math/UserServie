package com.example.demo.my.kafka.infra.kafka.publisher.kafka;

public interface DomainEvent<T> {
    void fire();
}