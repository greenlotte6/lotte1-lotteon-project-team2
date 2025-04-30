package kr.co.lotteon.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.service.user.UserService;
import kr.co.lotteon.entity.user.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class AutoLoginInterceptor implements HandlerInterceptor {

    private final UserService userService;

    public AutoLoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("sessUser") == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("autoLogin".equals(cookie.getName())) {
                        String uid = cookie.getValue();
                        Optional<User> userOpt = userService.findByUid(uid);
                        userOpt.ifPresent(user -> request.getSession().setAttribute("sessUser", user));
                    }
                }
            }
        }
        return true;
    }
}
