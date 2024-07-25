package User.Math.AI.domain.userProfile.repository;

import User.Math.AI.domain.userProfile.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, UserProfileRepositoryCustom {

}
