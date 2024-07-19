package User.Math.AI.domain.userProfile.dto.request;

import User.Math.AI.domain.school.entity.School;
import User.Math.AI.domain.user.entity.Users;
import lombok.Data;
import lombok.Getter;

@Data
public class AddInfoUserProfileRequest {
    private String email;

    private String nickName;

    private String schoolName;
}
