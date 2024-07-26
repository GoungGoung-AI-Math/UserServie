package User.Math.AI.domain.userProfile.repository;

import User.Math.AI.domain.userProfile.dto.response.UserProfileResponse;
import User.Math.AI.domain.userProfile.entity.QUserProfile;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserProfileRepositoryCustomImpl implements UserProfileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        QUserProfile userProfile = QUserProfile.userProfile;
        UserProfileResponse dto = queryFactory
                .select(Projections.constructor(
                        UserProfileResponse.class,
                        userProfile.school.name,
                        userProfile.nickName,
                        userProfile.profileImageUrl,
                        userProfile.users.createAt,
                        userProfile.tier,
                        userProfile.rating,
                        userProfile.solvedProblems.size().longValue(),
                        userProfile.questions.size().longValue(),
                        userProfile.answers.size().longValue(),
                        userProfile.likes.size().longValue()))
                .from(userProfile)
                .where(userProfile.users.id.eq(userId))
                .fetchOne();

        return dto;
    }
}
