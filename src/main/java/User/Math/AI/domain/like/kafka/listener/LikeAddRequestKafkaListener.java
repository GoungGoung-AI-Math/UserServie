package User.Math.AI.domain.like.kafka.listener;


import User.Math.AI.domain.like.mvc.dto.LikeAddRequest;
import User.Math.AI.domain.like.mvc.service.LikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import math.ai.my.kafka.infra.avrobuild.LikeAddRequestAvroModel;
import math.ai.my.kafka.infra.kafka.dtos.RelationType;
import math.ai.my.kafka.infra.kafka.listener.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeAddRequestKafkaListener implements KafkaConsumer<LikeAddRequestAvroModel> {

    private final LikeService likeService;

    @Override
    @KafkaListener(id = "${kafka-consumer.like-add-consumer-group-id}", topics = "${user-service.like-add-request-topic-name}")
    public void receive(@Payload List<LikeAddRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of like request received with keys:{}, partitions:{} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(avroModel -> {
            LikeAddRequest request = LikeAddRequest.builder()
                    .receiverId(avroModel.getReceiverId())
                    .giverId(avroModel.getGiverId())
                    .type(RelationType.valueOf(avroModel.getRelationType().name()))
                    .build();
            log.info("Processing successful like for receiver id: {} type : {}", request.getReceiverId(), request.getType());
            likeService.likeAddForUser(request);
        });

    }
}
