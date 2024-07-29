package User.Math.AI.domain.like.kafka.listener;


import User.Math.AI.my.kafka.infra.avrobuild.LikeAddRequestAvroModel;
import User.Math.AI.my.kafka.infra.kafka.listener.kafka.KafkaConsumer;

import java.util.List;

public class LikeAddRequestKafkaListener implements KafkaConsumer<LikeAddRequestAvroModel> {
    @Override
    public void receive(List<LikeAddRequestAvroModel> messages, List<String> keys, List<Integer> partitions, List<Long> offsets) {

    }
}
