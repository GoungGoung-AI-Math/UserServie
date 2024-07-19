package User.Math.AI;

import User.Math.AI.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class TestController {
    private final JwtDecoder jwtDecoder;

    @GetMapping("/userinfo")
    public String getUserInfo(@AuthenticationPrincipal CustomUserPrincipal principal) {
        if (principal == null) {
            throw new NullPointerException("Principal is null");
        }
        log.info("###################################");
        return principal.getUserId();
    }
    @GetMapping("/token")
    public Map<String, Object> getToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Jwt decodedJwt = jwtDecoder.decode(token);
        return decodedJwt.getClaims();
    }

    @GetMapping("/protected")
    public String protectedEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return "Hello, " + jwt.getClaimAsString("preferred_username");
    }
}


