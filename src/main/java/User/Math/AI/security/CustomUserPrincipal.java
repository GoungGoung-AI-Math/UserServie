package User.Math.AI.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class CustomUserPrincipal extends JwtAuthenticationToken {
    public CustomUserPrincipal(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    public String getUserId() {
        return this.getToken().getSubject();
    }

    public String getUsername() {
        return this.getToken().getClaimAsString("preferred_username");
    }

    public String getEmail() {
        return this.getToken().getClaimAsString("email");
    }
}
