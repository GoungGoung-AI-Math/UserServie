package math.ai.my.kafka.infra.kafka.publisher.kafka;

import math.ai.my.kafka.infra.kafka.publisher.kafka.DomainEvent;
public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}