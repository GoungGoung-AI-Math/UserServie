package User.Math.AI.domain.userProfile.controller;

import User.Math.AI.domain.userProfile.dto.request.AddInfoUserProfileRequest;
import User.Math.AI.domain.userProfile.dto.response.UserProfileResponse;
import User.Math.AI.domain.userProfile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-profile")
@Slf4j
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping("/add-info")
    public ResponseEntity<Map<String, String>> createUserProfile(@AuthenticationPrincipal Jwt jwt,
                                                                @RequestBody AddInfoUserProfileRequest request)
    {
        request.setEmail(jwt.getClaimAsString("preferred_username"));
        log.info("#################학교이름 : " + request.getSchoolName() + "##############");
        userProfileService.createUserProfile(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "회원 프로필 등록 완료");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                userProfileService.getUserProfile(Long.valueOf(jwt.getId())));
    }
}
