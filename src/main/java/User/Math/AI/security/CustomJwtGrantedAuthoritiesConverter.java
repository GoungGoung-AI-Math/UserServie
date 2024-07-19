package User.Math.AI.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // 'roles' 클레임에서 권한 추출
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) {
            roles = Collections.emptyList();
            log.info("'roles' claim is null, setting to empty list");
        }
        authorities.addAll(roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList()));

        log.info("Authorities from 'roles' claim: {}", authorities);

        // 'realm_access' 클레임에서 권한 추출
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null) {
            List<String> realmRoles = (List<String>) realmAccess.get("roles");
            if (realmRoles != null) {
                authorities.addAll(realmRoles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList()));
            }
        } else {
            log.info("'realm_access' claim is null, setting to empty map");
        }

        log.info("Authorities from 'realm_access' claim: {}", authorities);

        // 'resource_access' 클레임에서 권한 추출
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess != null) {
            resourceAccess.values().stream()
                    .flatMap(resource -> {
                        Object rolesObj = ((Map<String, Object>) resource).get("roles");
                        if (rolesObj instanceof List<?>) {
                            return ((List<?>) rolesObj).stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role));
                        } else {
                            return Stream.empty();
                        }
                    })
                    .forEach(authorities::add);
        } else {
            log.info("'resource_access' claim is null, setting to empty map");
        }

        log.info("Authorities from 'resource_access' claim: {}", authorities);

        return authorities;
    }
}
