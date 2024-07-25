package User.Math.AI.domain.userProfile.repository;

import User.Math.AI.domain.userProfile.dto.response.UserProfileResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

public interface UserProfileRepositoryCustom {
    UserProfileResponse getUserProfile(Long userId);
}
