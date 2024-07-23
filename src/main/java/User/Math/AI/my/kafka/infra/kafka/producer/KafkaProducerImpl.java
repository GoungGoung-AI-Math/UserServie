package User.Math.AI.my.kafka.infra.kafka.producer;

import User.Math.AI.my.kafka.infra.kafka.producer.exception.KafkaProducerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, K key, V message) {
        log.info("Sending message={} to topic={}", message, topicName);
        try {
            CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
            kafkaResultFuture.whenComplete((result, throwAble) -> {
                        if(throwAble == null) {
                            RecordMetadata metadata = result.getRecordMetadata();
                            log.info("Received successful response from Kafka for id: {}" +
                                            " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                                    key,
                                    metadata.topic(),
                                    metadata.partition(),
                                    metadata.offset(),
                                    metadata.timestamp());
                        } else {
                            log.error("Error while sending " + key +
                                    " message {} to topic {}", message.toString(), topicName, throwAble);
                            throw new RuntimeException(throwAble);
                        }
                    });
        } catch (KafkaException e) {
            log.error("Error on kafka producer with key: {}, message: {} and exception: {}", key, message,
                    e.getMessage());
            throw new KafkaProducerException("Error on kafka producer with key: " + key + " and message: " + message);
        }
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}