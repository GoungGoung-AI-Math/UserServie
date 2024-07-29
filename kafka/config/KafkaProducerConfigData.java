package Math.AI.my.kafka.infra.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.kafka-producer")
public class KafkaProducerConfigData {
    private String keySerializerClass;
    private String valueSerializerClass;
    private Integer batchSize;
    private Integer batchSizeBoostFactor;
    private Integer lingerMs;
    private String compressionType;
    private String acks;
    private Integer requestTimeoutMs;
    private Integer retryCount;
}