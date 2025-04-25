package kr.co.lotteon.security;

import kr.co.lotteon.oauth2.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    private final Oauth2UserService oauth2UserService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // 로그인 설정
        http.formLogin(login -> login
                .loginPage("/user/member/login")
                .defaultSuccessUrl("/")
                .failureUrl("/user/member/login?code=100")
                .usernameParameter("uid")
                .passwordParameter("pass"));

        // 로그아웃 설정
        http.logout(logout -> logout
                .logoutUrl("/user/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/user/member/login?code=101"));

        // ✅ OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/user/member/login") // OAuth2 로그인도 동일한 로그인 폼 사용
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oauth2UserService)) // 사용자 정보 후처리 서비스
                .defaultSuccessUrl("/") // OAuth2 로그인 성공 시 이동 경로 (선택)
        );
        
        
        // 인가 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/cs/*").hasAnyRole("USER", "SELLER", "ADMIN")
                .anyRequest().permitAll());

        // 기타 보안 설정
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // Security 암호화 인코더 설정
        return new BCryptPasswordEncoder();
    }

}