package User.Math.AI.domain.userProfile.kafka.listener;

import User.Math.AI.domain.userProfile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import math.ai.my.kafka.infra.avrobuild.UserProfileQuestionUpdateEvent;
import math.ai.my.kafka.infra.avrobuild.UserUpdateEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserUpdateEventConsumer {
    private final UserProfileService userProfileService;

    @KafkaListener(topics = "user-update-topic", groupId = "user-update-group")
    public void consume(UserUpdateEvent event) {
        log.info("Received UserUpdateEvent.avsc: {}", event);
        userProfileService.updateUserStatus(event);
    }

    @KafkaListener(topics = "user-profile-question-update-topic", groupId = "user-update-group")
    public void consume(UserProfileQuestionUpdateEvent event) {
        log.info("Received UserUpdateQuestion.avsc: {}", event);
        userProfileService.updateUserProfileQuestion(event);
    }
}
