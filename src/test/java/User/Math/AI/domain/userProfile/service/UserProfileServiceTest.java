package User.Math.AI.domain.userProfile.service;

import User.Math.AI.domain.school.entity.School;
import User.Math.AI.domain.school.repository.SchoolRepository;
import User.Math.AI.domain.user.entity.Users;
import User.Math.AI.domain.user.repository.UserRepository;
import User.Math.AI.domain.user.service.UserService;
import User.Math.AI.domain.userProfile.dto.request.AddInfoUserProfileRequest;
import User.Math.AI.domain.userProfile.entity.UserProfile;
import User.Math.AI.domain.userProfile.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserProfileServiceTest {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserService userService;

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

    @Test
    void registerUserAndCreateUserProfile() {
        // Given
        String email = "testuser@example.com";
        String name = "Test User";

        // Create a real JWT object
        Consumer<Map<String, Object>> claims = map -> {
            map.put("preferred_username", email);
            map.put("name", name);
        };

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claims(claims)
                .build();
        // Register user
        userService.registerUser(jwt);

        // Verify the user was created
        Optional<Users> userOptional = userRepository.findByEmail(email);
        assertTrue(userOptional.isPresent(), "User should be present in the database");

        // Create user profile
        AddInfoUserProfileRequest request = new AddInfoUserProfileRequest();
        request.setEmail(email);
        request.setNickName("TestNickName");
        request.setSchoolName(school.getName());

        userProfileService.createUserProfile(request);

        // Verify the user profile was created
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userOptional.get().getId());
        assertTrue(userProfileOptional.isPresent(), "UserProfile should be present in the database");
        assertEquals("TestNickName", userProfileOptional.get().getNickName(), "NickName should match the provided nickname");
        assertEquals(school.getName(), userProfileOptional.get().getSchool().getName(), "School name should match the provided school name");
    }
}