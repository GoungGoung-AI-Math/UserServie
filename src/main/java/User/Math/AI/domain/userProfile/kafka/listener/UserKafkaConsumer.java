package User.Math.AI.domain.userProfile.kafka.listener;

import User.Math.AI.domain.school.repository.SchoolRepository;
import User.Math.AI.domain.user.service.UserService;
import User.Math.AI.domain.userProfile.entity.UserProfile;
import User.Math.AI.domain.userProfile.repository.UserProfileRepository;
import User.Math.AI.domain.userProfile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import math.ai.my.kafka.infra.avrobuild.NicknameListAvro;
import math.ai.my.kafka.infra.avrobuild.UserIdListAvro;
import math.ai.my.kafka.infra.avrobuild.UserUpdateAttempt;
import math.ai.my.kafka.infra.avrobuild.UserUpdateTest;
import math.ai.my.kafka.infra.kafka.producer.KafkaProducer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserKafkaConsumer {

    private final UserProfileRepository userProfileRepository;
    private final SchoolRepository schoolRepository;
    private final UserProfileService userProfileService;
    private final KafkaProducer<String, NicknameListAvro> kafkaProducer;

    @KafkaListener(topics = "user-id-list-topic", groupId = "group_id")
    public void receive(UserIdListAvro message) {
        log.info("Received message: {}", message);

        // 메시지에서 사용자 ID 목록 추출
        List<Long> userIds = Arrays.stream(message.getUserIds().split(","))
                .filter(userId -> !userId.trim().isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toList());
        log.info("Extracted user IDs: {}", userIds);

        // 사용자 ID로 닉네임 조회
        List<String> nicknames = userProfileRepository.findNicknamesByUserIds(userIds);
        log.info("Found nicknames: {}", nicknames);

        // 닉네임 목록을 Avro 객체로 변환
        String nicknameList = String.join(",", nicknames);
        NicknameListAvro nicknameListAvro = NicknameListAvro.newBuilder()
                .setRequestId(message.getRequestId())
                .setNicknames(nicknameList)
                .build();
        log.info("Created NicknameListAvro: {}", nicknameListAvro);

        // Kafka에 메시지 전송
        kafkaProducer.send("nickname-list-topic2", null, nicknameListAvro);
        log.info("Sent NicknameListAvro to topic 'nickname-list-topic'");

    }

    @KafkaListener(topics = "user-update-attempt-topic", groupId = "group_id")
    public void receiveTest(UserUpdateAttempt message) {
        userProfileService.updateUserAttempt(message);
        log.info("###########updadt-attempt {}",message);
    }

    @KafkaListener(topics = "user-update-attempt-success-topic", groupId = "group_id")
    public void receiveAttemptSuccess(UserUpdateAttempt message) {
        userProfileService.updateUserAttemptSuccess(message);
        log.info("###########updadt-attempt-success {}",message);
    }
}