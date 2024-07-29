package Math.AI.my.kafka.infra.kafka.dtos.attempt.analysis;

import Math.AI.my.kafka.infra.kafka.dtos.MessageType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContentDto {
    private MessageType messageType;
    private String content;
}
