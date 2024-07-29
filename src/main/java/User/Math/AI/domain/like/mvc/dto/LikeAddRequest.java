package User.Math.AI.domain.like.mvc.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LikeAddRequest {
    private Long giverId; // 따봉 주는 사람
    private Long receiverId; // 따봉 받는 사람, 글쓴이
    private com.example.demo.my.kafka.infra.kafka.dtos.RelationType type;
    private Long relationId;
}