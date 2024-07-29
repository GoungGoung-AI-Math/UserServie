package math.ai.my.kafka.infra.kafka.publisher.kafka;

public interface DomainEvent<T> {
    void fire();
}