package math.ai.my.kafka.infra.kafka.listener;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}