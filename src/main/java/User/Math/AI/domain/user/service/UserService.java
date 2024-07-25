package User.Math.AI.domain.user.service;

import User.Math.AI.domain.user.entity.Users;
import User.Math.AI.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static User.Math.AI.domain.user.entity.Users.createUser;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public void registerUser(Jwt jwt) {
        String email = Optional.ofNullable(jwt.getClaimAsString("preferred_username"))
                .orElseThrow(() -> new IllegalArgumentException("JWT claim 'preferred_username' not found"));

        userRepository.findByEmail(email)
                .orElseGet(() -> {
                    Users newUser = createUser(email, jwt.getClaimAsString("name"));
                    return userRepository.save(newUser);
                });
    }
}
