package kr.co.lotteon.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();

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

        // ✅ 이전 요청 저장된 URL로 리다이렉트
        var savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            log.info("🔁 원래 요청한 URL로 리다이렉트: {}", targetUrl);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }



}