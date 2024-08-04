package User.Math.AI.domain.userProfile.service;

import User.Math.AI.domain.school.entity.School;
import User.Math.AI.domain.school.repository.SchoolRepository;
import User.Math.AI.domain.user.entity.Users;
import User.Math.AI.domain.user.repository.UserRepository;
import User.Math.AI.domain.userProfile.dto.request.AddInfoUserProfileRequest;
import User.Math.AI.domain.userProfile.dto.response.UserProfileResponse;
import User.Math.AI.domain.userProfile.entity.UserProfile;
import User.Math.AI.domain.userProfile.kafka.event.UserUpdateEvent;
import User.Math.AI.domain.userProfile.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import math.ai.my.kafka.infra.avrobuild.UserProfileQuestionUpdateEvent;
import math.ai.my.kafka.infra.avrobuild.UserUpdateAttempt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
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

//    public void attemptAfterUpdateUserData(Long userId, Long problemId, String status) {
//        UserProfile target = userProfileRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없음 userId: " + userId)).getUsers().getUserProfile();
//
//        target.addSolvedProblem(problemId);
//        userProfileRepository.save(target);
//    }

//    public void updateUserStatus(UserUpdateEvent event) {
//        Users user = userRepository.findById(event.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 유저 상태 업데이트 로직
//        UserProfile userProfile = userProfileRepository.findById(user.getId())
//                .orElseThrow(() -> new EntityNotFoundException("프로필 못찾음"));
//        userProfile.addSolvedProblem(event.getProblemId());
//        userProfileRepository.save(userProfile);
//    }

    public void updateUserAttempt(UserUpdateAttempt message) {
        Users user = userRepository.findById(message.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile userProfile = userProfileRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("프로필 못찾음"));
        userProfile.addAttemptProblem(message.getProblemId());
        userProfileRepository.save(userProfile);

        log.info("############name = {}", userProfile.getSchool().getName());
    }

    public void updateUserAttemptSuccess(UserUpdateAttempt message) {
        Users user = userRepository.findById(message.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile userProfile = userProfileRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("프로필 못찾음"));
        userProfile.addSolvedProblem(message.getProblemId());
        userProfileRepository.save(userProfile);

        // 나중에 school쪽으로 나누기
        School school = schoolRepository.findById(userProfile.getSchool().getId())
                .orElseThrow(() -> new EntityNotFoundException("학교 못찾음"));
        school.addTotalSolved();
        schoolRepository.save(school);
    }

    public void updateUserProfileQuestion(UserProfileQuestionUpdateEvent event) {
        Users user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile userProfile = userProfileRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("프로필 못찾음"));
        userProfile.addQuestion(event.getQuestionId());
        userProfileRepository.save(userProfile);
    }

    public UserProfileResponse getUserProfile(Long userId) {
        return userProfileRepository.getUserProfile(userId);
    }
}
