package User.Math.AI.domain.userProfile.controller;

import User.Math.AI.domain.userProfile.dto.request.AddInfoUserProfileRequest;
import User.Math.AI.domain.userProfile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-profile")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public ResponseEntity<String> createUserProfile(@AuthenticationPrincipal Jwt jwt,
                                                    AddInfoUserProfileRequest request)
    {
        request.setEmail(jwt.getClaimAsString("preferred_username"));
        userProfileService.createUserProfile(request);
        return ResponseEntity.ok("회원 프로필 등록 완료");
    }
}
