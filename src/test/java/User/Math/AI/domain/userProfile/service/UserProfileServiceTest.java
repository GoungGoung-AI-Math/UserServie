package User.Math.AI.domain.userProfile.service;

import User.Math.AI.domain.school.entity.School;
import User.Math.AI.domain.school.repository.SchoolRepository;
import User.Math.AI.domain.user.entity.Users;
import User.Math.AI.domain.user.repository.UserRepository;
import User.Math.AI.domain.userProfile.dto.request.AddInfoUserProfileRequest;
import User.Math.AI.domain.userProfile.entity.UserProfile;
import User.Math.AI.domain.userProfile.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserProfileServiceTest {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private Users user;
    private School school;

    @BeforeEach
    void setUp() {
        user = userRepository.save(Users.createUser("testuser@example.com", "Test User"));
        school = schoolRepository.save(new School(null, "Test School", 0L, 0L, 0.0, null));
    }

    @Test
    void createUserProfile() {
        AddInfoUserProfileRequest request = new AddInfoUserProfileRequest();
        request.setEmail(user.getEmail());
        request.setNickName("TestNickName");
        request.setSchoolName(school.getName());

        userProfileService.createUserProfile(request);

        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(user.getId());
        assertTrue(userProfileOptional.isPresent(), "UserProfile should be present in the database");
        assertEquals("TestNickName", userProfileOptional.get().getNickName(), "NickName should match the provided nickname");
        assertEquals(school.getName(), userProfileOptional.get().getSchool().getName(), "School name should match the provided school name");
    }
}