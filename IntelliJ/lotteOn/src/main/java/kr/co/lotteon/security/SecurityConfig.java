package kr.co.lotteon.security;

import jakarta.servlet.http.Cookie;
import kr.co.lotteon.oauth2.Oauth2UserService;
import kr.co.lotteon.repository.user.UserRepository;
import kr.co.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    private final Oauth2UserService oauth2UserService;
    private final UserRepository userRepository;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // 로그인 설정
        http.formLogin(login -> login
                .loginPage("/user/member/login")
                .defaultSuccessUrl("/")
                .failureUrl("/user/member/login?code=100")
                .usernameParameter("uid")
                .passwordParameter("pass")
                .failureHandler(authenticationFailureHandler()) // 실패 처리 핸들러 추가
                .successHandler(customLoginSuccessHandler)
        );

        // 로그아웃 설정
        http.logout(logout -> logout
                .logoutUrl("/user/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    // ✅ 쿠키 삭제
                    Cookie cookie = new Cookie("autoLogin", null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // 즉시 만료
                    response.addCookie(cookie);

                    // 세션 무효화
                    request.getSession().invalidate();

                    // 리다이렉트
                    response.sendRedirect("/user/member/login?code=101");
                })
                .invalidateHttpSession(true)
        );

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
                .requestMatchers("/my/*").hasAnyRole("USER", "SELLER", "ADMIN")
                .requestMatchers("/product/coupon").hasAnyRole("USER", "SELLER", "ADMIN")
                .requestMatchers("/product/cart").hasAnyRole("USER", "SELLER", "ADMIN")
                .requestMatchers("/product/addCart").hasAnyRole("USER", "SELLER", "ADMIN")
                .requestMatchers("/product/order").hasAnyRole("USER", "SELLER", "ADMIN")
                .requestMatchers("/product/order/direct").hasAnyRole("USER", "SELLER", "ADMIN")
                .requestMatchers("/product/ViewLoginCheck").hasAnyRole("USER", "SELLER", "ADMIN")
                .anyRequest().permitAll());


        // ✅ 자동 로그인 필터 등록
        http.addFilterBefore(autoLoginFilter(), UsernamePasswordAuthenticationFilter.class);

        // 기타 보안 설정
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // Security 암호화 인코더 설정
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AutoLoginFilter autoLoginFilter() {
        return new AutoLoginFilter(userRepository); // 필요하다면 생성자에 UserService 등 주입
    }


    // 인증 실패 핸들러 추가
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String errorCode = "100"; // 기본 에러 코드 (일반 로그인 실패)

            // 예외 유형에 따른 에러 코드 설정
            if (exception instanceof DisabledException) {
                errorCode = "102"; // 탈퇴한 회원 에러 코드
                log.warn("탈퇴한 회원 로그인 시도: {}", request.getParameter("uid"));
            } else if (exception instanceof BadCredentialsException) {
                errorCode = "100"; // 아이디/비밀번호 불일치
                log.warn("잘못된 자격 증명: {}", request.getParameter("uid"));
            } else {
                log.warn("로그인 실패: {}, 이유: {}", request.getParameter("uid"), exception.getMessage());
            }

            // 로그인 페이지로 리다이렉트 (에러 코드와 함께)
            response.sendRedirect("/user/member/login?code=" + errorCode);
        };
    }

}