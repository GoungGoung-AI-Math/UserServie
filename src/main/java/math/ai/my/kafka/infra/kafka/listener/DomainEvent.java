package math.ai.my.kafka.infra.kafka.listener;

public interface DomainEvent<T> {
    void fire();
}