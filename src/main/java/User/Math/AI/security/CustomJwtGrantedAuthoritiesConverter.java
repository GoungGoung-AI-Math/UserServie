package User.Math.AI.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // 기본 권한 추출
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) {
            roles = Collections.emptyList();
            log.info("'roles' claim is null, setting to empty list");
        }
        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        log.info("Authorities from 'roles' claim: {}", authorities);

        // 추가로 JWT의 클레임에서 역할 정보를 추출하여 GrantedAuthority로 변환
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess == null) {
            resourceAccess = Collections.emptyMap();
            log.info("'resource_access' claim is null, setting to empty map");
        }
        Collection<GrantedAuthority> resourceAccessAuthorities = resourceAccess.values().stream()
                .flatMap(resource -> {
                    Object rolesObj = ((Map<String, Object>) resource).get("roles");
                    if (rolesObj instanceof List<?>) {
                        return ((List<?>) rolesObj).stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role));
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());

        log.info("Authorities from 'resource_access' claim: {}", resourceAccessAuthorities);

        return Stream.concat(authorities.stream(), resourceAccessAuthorities.stream())
                .collect(Collectors.toList());
    }
}
