package User.Math.AI.my.kafka.infra.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "problem-service")
public class ProblemServiceKafkaConfigData {
    private String AttemptAnalysisRequestTopicName;
    private String AttemptAnalysisResponseTopicName;
    private String LikeAddRequestTopicName;
}