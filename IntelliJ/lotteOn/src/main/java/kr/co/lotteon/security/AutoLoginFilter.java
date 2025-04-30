package kr.co.lotteon.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AutoLoginFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("autoLogin".equals(cookie.getName())) {
                        String uid = cookie.getValue();
                        log.info("✅ 자동 로그인 쿠키 발견: uid={}", uid);

                        Optional<User> optUser = userRepository.findById(uid);
                        if (optUser.isPresent()) {
                            User user = optUser.get();

                            MyUserDetails userDetails = MyUserDetails.builder()
                                    .user(user)
                                    .build();

                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            log.info("✅ 자동 로그인 인증 완료 → SecurityContext 설정됨");
                        }

                        break; // ✅ 쿠키 1개만 처리
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}