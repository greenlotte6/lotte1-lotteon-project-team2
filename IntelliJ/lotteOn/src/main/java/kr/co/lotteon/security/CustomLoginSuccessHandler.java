package kr.co.lotteon.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 파라미터 체크
        String autoLogin = request.getParameter("autoLogin");
        log.info("✅ 로그인 성공 - autoLogin param: {}", autoLogin);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        if ("true".equals(autoLogin)) {
            // 자동 로그인 쿠키 발급
            Cookie cookie = new Cookie("autoLogin", userDetails.getUsername());
            cookie.setMaxAge(60 * 60 * 24 * 7); // 7일
            cookie.setPath("/");
            response.addCookie(cookie);
            log.info("✅ 자동 로그인 쿠키 발급 완료 (uid: {})", userDetails.getUsername());
        } else {
            // 세션 쿠키 명시 설정 (브라우저 종료 시 삭제)
            Cookie sessionCookie = new Cookie("JSESSIONID", request.getSession().getId());
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(-1); // 세션 쿠키
            response.addCookie(sessionCookie);
            log.info("❌ 자동 로그인 아님 → 세션 쿠키 설정 완료");
        }

        // 기본 성공 경로로 이동
        super.onAuthenticationSuccess(request, response, authentication);
    }



}
