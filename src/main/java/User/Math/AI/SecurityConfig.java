package User.Math.AI;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfig corsConfig;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                // CSRF 설정 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                // 세션 관리를 STATELESS로 설정
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청에 대한 접근 제한 설정
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/user/**").permitAll()
//                        .requestMatchers(
//                                "/api/auth/**").permitAll()
//                        .requestMatchers(
//                                "/oauth/**").permitAll()
////                        .anyRequest().authenticated())
//                        .anyRequest().authenticated())
                        .anyRequest().permitAll());

        return httpSecurity.build();
    }
}
