package User.Math.AI.my.kafka.infra.kafka.producer;

import User.Math.AI.my.kafka.infra.kafka.config.KafkaConfigData;
import User.Math.AI.my.kafka.infra.kafka.config.KafkaProducerConfigData;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducerConfigData kafkaProducerConfigData;

    public KafkaProducerConfig(KafkaConfigData kafkaConfigData,
                               KafkaProducerConfigData kafkaProducerConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducerConfigData = kafkaProducerConfigData;
    }

    /**
     * ProducerConfig는 카프카 자체에서 제공하는 설정을
     * application.properties에 작성된 값으로 주입
     *
     * @return
     */
    @Bean
    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getKeySerializerClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getValueSerializerClass());
        // 배치 크기
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigData.getBatchSize() *
                kafkaProducerConfigData.getBatchSizeBoostFactor());
        // Sender 스레드가 메세지를 묶은 배치를 보내기 위한 최대 대기 시간
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.getLingerMs());
        // 압축 알고리즘
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.getCompressionType());
        // 프로듀서가 브로커의 확인을 기다리는지 설정, 0 : 안기다림, 1 : leader까지만 확인(follower에 복제하다가 실패해도 모름) ,2 : all(-1) : follower까지 받았는지 확인
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getAcks());
        // 컨슈머가 브로커로부터 기다릴 수 있는 최대 대기 시간
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerConfigData.getRequestTimeoutMs());
        // 실패했을 때 재 시도하는 횟수
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.getRetryCount());
        return props;
    }

    @Bean
    public ProducerFactory<K, V> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<K, V> kafkaTemplate() {
        /**
         * KafkaTemplate : Kafka 클러스터에 데이터를 보내기 위한 래퍼 클래스
         * producerFactory로 만든 프로듀서는 데이터를 생산해서 클러스터로 보냄
         */
        return new KafkaTemplate<>(producerFactory());
    }
}