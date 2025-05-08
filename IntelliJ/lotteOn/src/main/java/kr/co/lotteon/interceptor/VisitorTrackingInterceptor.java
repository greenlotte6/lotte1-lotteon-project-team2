package kr.co.lotteon.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lotteon.service.visitor.VisitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Interceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class VisitorTrackingInterceptor implements HandlerInterceptor {

    private final VisitorService visitorService;
    private static final String VISITOR_COOKIE_NAME = "visitor_id";
    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 30; // 30일

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {

        // 1. 쿠키에서 방문자 ID 가져오기
        String visitorId = getVisitorIdFromCookie(request);


        // 2. 쿠키가 없으면 새로 생성
        if (visitorId == null) {
            visitorId = UUID.randomUUID().toString();
            addVisitorCookie(response, visitorId);
        }

        // 3. 방문자 추적 (Redis에 저장)
        visitorService.trackVisitor(visitorId);

        return true;    // 항상 요청 처리 계속

    }


    private String getVisitorIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(c -> VISITOR_COOKIE_NAME.equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private void addVisitorCookie(HttpServletResponse response, String visitorId) {
        Cookie cookie = new Cookie(VISITOR_COOKIE_NAME, visitorId);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

}
