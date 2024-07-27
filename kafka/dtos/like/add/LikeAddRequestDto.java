package com.example.demo.my.kafka.infra.kafka.dtos.like.add;

import com.example.demo.domain.like.mvc.dto.LikeAddRequest;
import com.example.demo.my.kafka.infra.kafka.dtos.DomainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeAddRequestDto {
    private Long likeId;
    private Long giverId; // 따봉 주는 사람
    private Long receiverId; // 따봉 받는 사람, 글쓴이
    private DomainType type;
    private Long domainId;

    public LikeAddRequestDto(Long likeId, LikeAddRequest request){
        this.likeId = likeId;
        this.giverId = request.getGiverId();
        this.receiverId = request.getReceiverId();
        this.type = request.getType();
        this.domainId = request.getDomainId();
    }
}
