package User.Math.AI.my.kafka.infra.kafka.publisher.kafka;

public interface DomainEvent<T> {
    void fire();
}