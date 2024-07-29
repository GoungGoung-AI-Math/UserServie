package User.Math.AI.domain.userProfile.kafka.event;

import User.Math.AI.domain.userProfile.dto.request.UserUpdateMessage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import math.ai.my.kafka.infra.kafka.listener.DomainEvent;
import math.ai.my.kafka.infra.kafka.listener.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdateEvent implements DomainEvent<UserUpdateMessage> {
    private final UserUpdateMessage userUpdateMessage;
    private final List<String> failureMessages;
    private final ZonedDateTime createdAt;
    private final DomainEventPublisher<UserUpdateEvent> userUpdateEventPublisher;

    @Override
    public void fire() {
        userUpdateEventPublisher.publish(this);
    }
}
