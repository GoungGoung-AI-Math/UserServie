package User.Math.AI.domain.userProfile.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateMessage {
    private Long userId;
    private Long problemId;
    private String status;
}
