package User.Math.AI.domain.user.service;

import User.Math.AI.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static User.Math.AI.domain.user.entity.Users.createUser;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public void registerUser(Jwt jwt) {
        userRepository.findByEmail(jwt.getClaimAsString("preferred_username"))
                .orElseGet(() ->
                        createUser(jwt.getClaimAsString("preferred_username")
                                , jwt.getClaimAsString("name")));
    }

}
