package User.Math.AI.domain.user.controller;

import User.Math.AI.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@AuthenticationPrincipal Jwt jwt) {
        userService.registerUser(jwt);
        Map<String, String> response = new HashMap<>();
        response.put("message", "유저 등록 성공");
        return ResponseEntity.ok(response);
    }
}
