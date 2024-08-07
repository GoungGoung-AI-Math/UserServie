//package User.Math.AI.domain.user.service;
//
//import User.Math.AI.domain.user.entity.Users;
//import User.Math.AI.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Map;
//import java.util.Optional;
//import java.util.function.Consumer;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void registerUserIntegrationTest() {
//        // Given
//        String email = "testuser@example.com";
//        String name = "Test User";
//
//        Consumer<Map<String, Object>> claims = map -> {
//            map.put("preferred_username", email);
//            map.put("name", name);
//        };
//
//        Jwt jwt = Jwt.withTokenValue("token")
//                .header("alg", "none")
//                .claims(claims)
//                .build();
//
//        // When
//        userService.registerUser(jwt);
//
//        // Then
//        Optional<Users> userOptional = userRepository.findByEmail(email);
//        assertTrue(userOptional.isPresent(), "User should be present in the database");
//        assertEquals(name, userOptional.get().getName(), "User name should match the provided name");
//    }
//
//    @Test
//    void registerExistingUserTest() {
//        // Given
//        String email = "existinguser@example.com";
//        String name = "Existing User";
//
//        Users existingUser = Users.builder()
//                .email(email)
//                .name(name)
//                .phone("test")
//                .password("test")
//                .build();
//        userRepository.save(existingUser);
//
//        Consumer<Map<String, Object>> claims = map -> {
//            map.put("preferred_username", email);
//            map.put("name", name);
//        };
//
//        Jwt jwt = Jwt.withTokenValue("token")
//                .header("alg", "none")
//                .claims(claims)
//                .build();
//
//        // When
//        userService.registerUser(jwt);
//
//        // Then
//        Optional<Users> userOptional = userRepository.findByEmail(email);
//        assertTrue(userOptional.isPresent(), "User should be present in the database");
//        assertEquals(name, userOptional.get().getName(), "User name should match the provided name");
//
//        long userCount = userRepository.count();
//        assertEquals(1, userCount, "There should be only one user in the database with the given email");
//    }
//
//    @Test
//    void registerUserWithInvalidJwtTest() {
//        // Given
//        Jwt jwt = Jwt.withTokenValue("token")
//                .header("alg", "none")
//                .claim("some_claim", "some_value")  // 최소한의 클레임 추가
//                .build();
//
//        // When
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            userService.registerUser(jwt);
//        });
//
//        // Then
//        String expectedMessage = "JWT claim 'preferred_username' not found";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//}