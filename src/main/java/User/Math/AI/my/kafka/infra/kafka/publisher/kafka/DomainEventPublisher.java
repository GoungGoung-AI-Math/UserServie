package com.example.demo.my.kafka.infra.kafka.publisher.kafka;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}