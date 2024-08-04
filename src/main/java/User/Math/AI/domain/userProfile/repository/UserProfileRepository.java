package User.Math.AI.domain.userProfile.repository;

import User.Math.AI.domain.userProfile.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, UserProfileRepositoryCustom {

    @Query("SELECT u.nickName FROM UserProfile u WHERE u.id IN :userIds")
    List<String> findNicknamesByUserIds(@Param("userIds") List<Long> userIds);
}
