package math.ai.my.kafka.infra.kafka.listener;

import math.ai.my.kafka.infra.kafka.publisher.kafka.DomainEvent;
public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}