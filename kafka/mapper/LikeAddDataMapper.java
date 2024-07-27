package com.example.demo.my.kafka.infra.kafka.mapper;

import com.example.demo.my.kafka.infra.avrobuild.DomainType;
import com.example.demo.my.kafka.infra.avrobuild.LikeAddRequestAvroModel;
import com.example.demo.my.kafka.infra.kafka.dtos.like.add.LikeAddRequestDto;
import org.springframework.stereotype.Component;

@Component
public class LikeAddDataMapper {
    public LikeAddRequestAvroModel likeAddRequestToAvroModel(LikeAddRequestDto request){
        return LikeAddRequestAvroModel.newBuilder()
                .setLikeId(request.getLikeId())
                .setDomainId(request.getDomainId())
                .setGiverId(request.getGiverId())
                .setReceiverId(request.getReceiverId())
                .setDomainType(DomainType.valueOf(request.getType().name()))
                .build();
    }
}
