package User.Math.AI.domain.userProfile.kafka.listener;

import User.Math.AI.domain.userProfile.dto.request.UserUpdateMessage;
import User.Math.AI.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUpdateKafkaListener {
    private final UserService userService;

    @KafkaListener(id = "${kafka-consumer.user-update-consumer-group-id}", topics = "${problem-service.user-update-topic-name}")
    public void receive(@Payload List<UserUpdateMessage> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of user update messages received with keys:{}, partitions:{} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(message -> {
            log.info("Processing user update for user id: {} problem id: {} status: {}",
                    message.getUserId(), message.getProblemId(), message.getStatus());
            userService.updateUserStatus(message.getUserId(), message.getProblemId(), message.getStatus());
        });
    }
}
