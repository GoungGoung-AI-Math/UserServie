package User.Math.AI.domain.userProfile.service;

import User.Math.AI.domain.school.entity.School;
import User.Math.AI.domain.school.repository.SchoolRepository;
import User.Math.AI.domain.user.entity.Users;
import User.Math.AI.domain.user.repository.UserRepository;
import User.Math.AI.domain.userProfile.dto.request.AddInfoUserProfileRequest;
import User.Math.AI.domain.userProfile.entity.UserProfile;
import User.Math.AI.domain.userProfile.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    public void createUserProfile(AddInfoUserProfileRequest addInfoUserProfileRequest) {
        Users targetUser = userRepository.findByEmail(addInfoUserProfileRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없음 email: " + addInfoUserProfileRequest.getEmail()));

        School school = schoolRepository.findByName(addInfoUserProfileRequest.getSchoolName())
                .orElseThrow(() -> new EntityNotFoundException("학교를 찾을 수 없음 학교이름: " + addInfoUserProfileRequest.getSchoolName()));

        UserProfile userProfile = UserProfile
                .createUserProfile(targetUser, school, addInfoUserProfileRequest.getNickName());

        userProfileRepository.save(userProfile);
    }
}
